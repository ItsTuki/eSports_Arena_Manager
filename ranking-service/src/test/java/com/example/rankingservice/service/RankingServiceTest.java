package com.example.rankingservice.service;

import com.example.rankingservice.dto.RankingDtos.RankingRequest;
import com.example.rankingservice.dto.RankingDtos.RankingUpdate;
import com.example.rankingservice.model.Ranking;
import com.example.rankingservice.repository.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {

    @Mock
    private RankingRepository repository;

    @InjectMocks
    private RankingService rankingService;

    private RankingRequest rankingRequestValido;
    private Ranking rankingParticipanteA;
    private Ranking rankingParticipanteB;

    @BeforeEach
    void setUp() {
        rankingRequestValido = new RankingRequest(1L, 100L); // torneoId=1, participanteId=100

        rankingParticipanteA = new Ranking();
        rankingParticipanteA.setId(1L);
        rankingParticipanteA.setTorneoId(1L);
        rankingParticipanteA.setParticipanteId(100L);
        rankingParticipanteA.setPuntos(10);
        rankingParticipanteA.setDiferencia(5);
        rankingParticipanteA.setPosicion(1);
        rankingParticipanteA.setCerrado(false);

        rankingParticipanteB = new Ranking();
        rankingParticipanteB.setId(2L);
        rankingParticipanteB.setTorneoId(1L);
        rankingParticipanteB.setParticipanteId(200L);
        rankingParticipanteB.setPuntos(5);
        rankingParticipanteB.setDiferencia(2);
        rankingParticipanteB.setPosicion(2);
        rankingParticipanteB.setCerrado(false);
    }

    @Test
    void crear_ParticipanteNuevo_DebeGuardarRankingYRecalcular() {
        // Arrange
        // Simulamos que no hay registros previos
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(repository.save(any(Ranking.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Ranking resultado = rankingService.crear(rankingRequestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getTorneoId());
        assertEquals(100L, resultado.getParticipanteId());

        // Verifica que se llamó a save al menos una vez (al crear y/o al recalcular)
        verify(repository, atLeastOnce()).save(any(Ranking.class));
    }

    @Test
    void crear_ParticipanteDuplicado_DebeLanzarExcepcion() {
        // Arrange
        // Simulamos que el participanteA ya está en el ranking
        when(repository.findAll()).thenReturn(List.of(rankingParticipanteA));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            rankingService.crear(rankingRequestValido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No duplicar participante en ranking", exception.getReason());
        verify(repository, never()).save(any());
    }

    @Test
    void listar_PorTorneo_DebeRetornarListaOrdenadaPorPosicion() {
        // Arrange
        // Insertamos desordenados a propósito para probar el sorting
        when(repository.findAll()).thenReturn(Arrays.asList(rankingParticipanteB, rankingParticipanteA));

        // Act
        List<Ranking> resultados = rankingService.listar(1L);

        // Assert
        assertEquals(2, resultados.size());
        assertEquals(100L, resultados.get(0).getParticipanteId()); // Posicion 1
        assertEquals(200L, resultados.get(1).getParticipanteId()); // Posicion 2
    }

    @Test
    void posicion_ParticipanteExistente_DebeRetornarRankingCorrecto() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(rankingParticipanteA, rankingParticipanteB));

        // Act
        Ranking resultado = rankingService.posicion(1L, 200L);

        // Assert
        assertNotNull(resultado);
        assertEquals(200L, resultado.getParticipanteId());
        assertEquals(2, resultado.getPosicion());
    }

    @Test
    void actualizar_ResultadoNoValidado_DebeLanzarExcepcion() {
        // Arrange
        RankingUpdate updateInvalido = new RankingUpdate(15, 3, 0, 10, false); // No validado

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            rankingService.actualizar(1L, updateInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Solo resultados validados actualizan ranking", exception.getReason());
    }

    @Test
    void actualizar_RankingCerrado_DebeLanzarExcepcion() {
        // Arrange
        rankingParticipanteA.setCerrado(true);
        when(repository.findById(1L)).thenReturn(Optional.of(rankingParticipanteA));
        RankingUpdate update = new RankingUpdate(15, 3, 0, 10, true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            rankingService.actualizar(1L, update);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Ranking cerrado", exception.getReason());
    }

    @Test
    void actualizar_DatosValidos_DebeActualizarYRecalcular() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(rankingParticipanteA));
        // Al recalcular, listará los rankings de la base de datos
        when(repository.findAll()).thenReturn(List.of(rankingParticipanteA));
        when(repository.save(any(Ranking.class))).thenAnswer(i -> i.getArgument(0));

        RankingUpdate update = new RankingUpdate(15, 3, 0, 10, true);

        // Act
        Ranking resultado = rankingService.actualizar(1L, update);

        // Assert
        assertEquals(15, resultado.getPuntos());
        assertEquals(3, resultado.getVictorias());
        assertEquals(10, resultado.getDiferencia());
        verify(repository, atLeastOnce()).save(any(Ranking.class));
    }

    @Test
    void cerrar_Torneo_DebeCambiarEstadoACerrado() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(rankingParticipanteA));
        when(repository.save(any(Ranking.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        List<Ranking> resultados = rankingService.cerrar(1L);

        // Assert
        assertTrue(resultados.get(0).isCerrado());
        verify(repository, atLeastOnce()).save(any(Ranking.class));
    }

    @Test
    void reiniciar_Torneo_DebeResetearEstadisticas() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(rankingParticipanteA));
        when(repository.save(any(Ranking.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        List<Ranking> resultados = rankingService.reiniciar(1L);

        // Assert
        Ranking res = resultados.get(0);
        assertEquals(0, res.getPuntos());
        assertEquals(0, res.getVictorias());
        assertEquals(0, res.getDerrotas());
        assertEquals(0, res.getDiferencia());
        assertFalse(res.isCerrado());
    }
}