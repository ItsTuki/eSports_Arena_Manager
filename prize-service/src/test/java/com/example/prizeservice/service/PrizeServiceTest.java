package com.example.prizeservice.service;

import com.example.prizeservice.client.RankingClient;
import com.example.prizeservice.client.RankingClient.RankingResponse;
import com.example.prizeservice.client.TournamentClient;
import com.example.prizeservice.client.TournamentClient.TorneoResponse;
import com.example.prizeservice.dto.PremioDtos.AsignarRequest;
import com.example.prizeservice.dto.PremioDtos.PremioRequest;
import com.example.prizeservice.dto.PremioDtos.PremioUpdate;
import com.example.prizeservice.model.Premio;
import com.example.prizeservice.model.PremioAsignado;
import com.example.prizeservice.repository.PremioAsignadoRepository;
import com.example.prizeservice.repository.PremioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrizeServiceTest {

    @Mock
    private PremioRepository repository;

    @Mock
    private PremioAsignadoRepository asignadoRepository;

    @Mock
    private TournamentClient tournamentClient;

    @Mock
    private RankingClient rankingClient;

    @InjectMocks
    private PremioService premioService;

    private PremioRequest premioRequestValido;
    private Premio premioActivo;
    private Premio premioAsignado;

    @BeforeEach
    void setUp() {
        premioRequestValido = new PremioRequest(
                1L, // torneoId
                1,  // posicion
                "Primer Lugar - Copa Oro",
                new BigDecimal("500.00")
        );

        premioActivo = new Premio();
        premioActivo.setId(10L);
        premioActivo.setTorneoId(1L);
        premioActivo.setPosicion(1);
        premioActivo.setDescripcion("Primer Lugar - Copa Oro");
        premioActivo.setValor(new BigDecimal("500.00"));
        premioActivo.setEstado("ACTIVO");

        premioAsignado = new Premio();
        premioAsignado.setId(11L);
        premioAsignado.setTorneoId(1L);
        premioAsignado.setPosicion(2);
        premioAsignado.setDescripcion("Segundo Lugar");
        premioAsignado.setValor(new BigDecimal("250.00"));
        premioAsignado.setEstado("ASIGNADO");
    }

    @Test
    void crear_PremioNuevo_DebeGuardarYRetornar() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList()); // Sin duplicados
        when(repository.save(any(Premio.class))).thenReturn(premioActivo);

        // Act
        Premio resultado = premioService.crear(premioRequestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getPosicion());
        assertEquals("Primer Lugar - Copa Oro", resultado.getDescripcion());
        verify(repository, times(1)).save(any(Premio.class));
    }

    @Test
    void crear_PosicionDuplicadaEnTorneo_DebeLanzarExcepcion() {
        // Arrange
        // Simulamos que ya existe un premio registrado para la posición 1 en ese torneo
        when(repository.findAll()).thenReturn(List.of(premioActivo));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            premioService.crear(premioRequestValido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No duplicar premio para la misma posicion", exception.getReason());
        verify(repository, never()).save(any());
    }

    @Test
    void listar_ConFiltros_DebeRetornarListaFiltrada() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(premioActivo, premioAsignado));

        // Act - Filtrar por torneoId 1 y posicion 1
        List<Premio> resultados = premioService.listar(1L, 1);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals(10L, resultados.get(0).getId());
    }

    @Test
    void buscar_PremioExistente_DebeRetornarPremio() {
        // Arrange
        when(repository.findById(10L)).thenReturn(Optional.of(premioActivo));

        // Act
        Premio resultado = premioService.buscar(10L);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
    }

    @Test
    void buscar_PremioInexistente_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            premioService.buscar(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Premio no encontrado", exception.getReason());
    }

    @Test
    void actualizar_PremioAsignadoSinAutorizacion_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(11L)).thenReturn(Optional.of(premioAsignado));

        // request sin autorización (autorizado = false o null)
        PremioUpdate updateInvalido = new PremioUpdate("Nueva desc", null, null, false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            premioService.actualizar(11L, updateInvalido);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("No modificar premio asignado sin autorizacion", exception.getReason());
    }

    @Test
    void actualizar_PremioAsignadoConAutorizacion_DebeActualizarYGuardar() {
        // Arrange
        when(repository.findById(11L)).thenReturn(Optional.of(premioAsignado));
        when(repository.save(any(Premio.class))).thenAnswer(i -> i.getArgument(0));

        PremioUpdate updateValido = new PremioUpdate("Segunda Plaza Modificada", new BigDecimal("300.00"), null, true);

        // Act
        Premio resultado = premioService.actualizar(11L, updateValido);

        // Assert
        assertEquals("Segunda Plaza Modificada", resultado.getDescripcion());
        assertEquals(new BigDecimal("300.00"), resultado.getValor());
        verify(repository, times(1)).save(any(Premio.class));
    }

    @Test
    void desactivar_PremioAsignado_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(11L)).thenReturn(Optional.of(premioAsignado));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            premioService.desactivar(11L);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Premio ya asignado", exception.getReason());
    }

    @Test
    void desactivar_PremioActivo_DebeCambiarEstadoAInactivo() {
        // Arrange
        when(repository.findById(10L)).thenReturn(Optional.of(premioActivo));
        when(repository.save(any(Premio.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Premio resultado = premioService.desactivar(10L);

        // Assert
        assertEquals("INACTIVO", resultado.getEstado());
        verify(repository, times(1)).save(premioActivo);
    }

    @Test
    void asignar_CondicionesCumplidas_DebeCambiarEstadoYRegistrarPremioAsignado() {
        // Arrange
        when(repository.findById(10L)).thenReturn(Optional.of(premioActivo));

        // Simulamos los clientes Feign para validar fin del torneo y ranking validado/cerrado
        when(tournamentClient.buscar(1L)).thenReturn(new TorneoResponse(1L, "FINALIZADO"));
        when(rankingClient.listar(1L)).thenReturn(List.of(new RankingResponse(1L, 500L, 1, true)));

        when(repository.save(any(Premio.class))).thenAnswer(i -> i.getArgument(0));

        PremioAsignado mockAsignado = new PremioAsignado();
        mockAsignado.setId(1L);
        mockAsignado.setPremioId(10L);
        mockAsignado.setParticipanteId(500L);
        when(asignadoRepository.save(any(PremioAsignado.class))).thenReturn(mockAsignado);

        AsignarRequest request = new AsignarRequest(500L, null, null);

        // Act
        PremioAsignado resultado = premioService.asignar(10L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getPremioId());
        assertEquals(500L, resultado.getParticipanteId());
        assertEquals("ASIGNADO", premioActivo.getEstado());
        verify(repository).save(premioActivo);
        verify(asignadoRepository).save(any(PremioAsignado.class));
    }

    @Test
    void asignar_TorneoNoFinalizado_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(10L)).thenReturn(Optional.of(premioActivo));
        when(tournamentClient.buscar(1L)).thenReturn(new TorneoResponse(1L, "EN_CURSO"));

        AsignarRequest request = new AsignarRequest(500L, null, null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            premioService.asignar(10L, request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("No asignar premios antes de finalizar"));
    }
}