package com.example.matchservice.service;

import com.example.matchservice.client.RegistrationClient;
import com.example.matchservice.client.RegistrationClient.InscripcionResponse;
import com.example.matchservice.client.TournamentClient;
import com.example.matchservice.client.TournamentClient.TorneoResponse;
import com.example.matchservice.dto.PartidaDtos.PartidaRequest;
import com.example.matchservice.dto.PartidaDtos.PartidaUpdate;
import com.example.matchservice.model.Partida;
import com.example.matchservice.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private PartidaRepository repository;

    @Mock
    private TournamentClient tournamentClient;

    @Mock
    private RegistrationClient registrationClient;

    @InjectMocks
    private PartidaService partidaService;

    private PartidaRequest partidaRequestValida;
    private Partida partidaProgramada;

    @BeforeEach
    void setUp() {
        partidaRequestValida = new PartidaRequest(
                1L, // torneoId
                10L, // participanteAId
                20L, // participanteBId
                1, // ronda
                LocalDateTime.now().plusDays(2),
                true, // participanteAInscrito
                true  // participanteBInscrito
        );

        partidaProgramada = new Partida();
        partidaProgramada.setId(1L);
        partidaProgramada.setTorneoId(1L);
        partidaProgramada.setParticipanteAId(10L);
        partidaProgramada.setParticipanteBId(20L);
        partidaProgramada.setRonda(1);
        partidaProgramada.setFechaHora(LocalDateTime.now().plusDays(2));
        partidaProgramada.setEstado("PROGRAMADA");
    }

    @Test
    void crear_PartidaValida_DebeGuardarYRetornarPartida() {
        // Arrange
        when(tournamentClient.buscar(1L)).thenReturn(new TorneoResponse(1L, "ACTIVO"));

        // Simulamos que ambos participantes están inscritos
        List<InscripcionResponse> inscripciones = Arrays.asList(
                new InscripcionResponse(10L, null, "ACEPTADA"),
                new InscripcionResponse(20L, null, "ACEPTADA")
        );
        when(registrationClient.listar(1L)).thenReturn(inscripciones);
        when(repository.findAll()).thenReturn(Collections.emptyList()); // Sin duplicados
        when(repository.save(any(Partida.class))).thenReturn(partidaProgramada);

        // Act
        Partida resultado = partidaService.crear(partidaRequestValida);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getParticipanteAId());
        assertEquals(20L, resultado.getParticipanteBId());
        verify(repository, times(1)).save(any(Partida.class));
    }

    @Test
    void crear_MismoParticipante_DebeLanzarExcepcion() {
        // Arrange
        when(tournamentClient.buscar(1L)).thenReturn(new TorneoResponse(1L, "ACTIVO"));
        PartidaRequest requestInvalido = new PartidaRequest(
                1L, 10L, 10L, 1, LocalDateTime.now(), true, true
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            partidaService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Participantes deben ser distintos", exception.getReason());
        verify(repository, never()).save(any(Partida.class));
    }

    @Test
    void crear_ParticipanteNoInscritoEnCliente_DebeLanzarExcepcion() {
        // Arrange
        when(tournamentClient.buscar(1L)).thenReturn(new TorneoResponse(1L, "ACTIVO"));

        // Solo el participante A está en la lista del registration-service
        List<InscripcionResponse> inscripciones = List.of(
                new InscripcionResponse(10L, null, "ACEPTADA")
        );
        when(registrationClient.listar(1L)).thenReturn(inscripciones);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            partidaService.crear(partidaRequestValida);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No crear partida con participante no inscrito", exception.getReason());
    }

    @Test
    void crear_PartidaDuplicada_DebeLanzarExcepcion() {
        // Arrange
        when(tournamentClient.buscar(1L)).thenReturn(new TorneoResponse(1L, "ACTIVO"));
        List<InscripcionResponse> inscripciones = Arrays.asList(
                new InscripcionResponse(10L, null, "ACEPTADA"),
                new InscripcionResponse(20L, null, "ACEPTADA")
        );
        when(registrationClient.listar(1L)).thenReturn(inscripciones);

        // Simulamos que ya existe una partida en esa ronda con los mismos participantes
        when(repository.findAll()).thenReturn(List.of(partidaProgramada));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            partidaService.crear(partidaRequestValida);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No duplicar enfrentamiento en la misma ronda", exception.getReason());
    }

    @Test
    void buscar_PartidaExistente_DebeRetornarPartida() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(partidaProgramada));

        // Act
        Partida resultado = partidaService.buscar(1L);

        // Assert
        assertEquals(1L, resultado.getId());
    }

    @Test
    void actualizar_CambiarEstadoACancelada_DebePermitirCambio() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(partidaProgramada));
        when(repository.save(any(Partida.class))).thenAnswer(i -> i.getArgument(0));

        PartidaUpdate update = new PartidaUpdate(null, null, null, "EN_CURSO");

        // Act
        Partida resultado = partidaService.actualizar(1L, update);

        // Assert
        assertEquals("EN_CURSO", resultado.getEstado());
    }

    @Test
    void actualizar_PartidaCanceladaAEnCurso_DebeLanzarExcepcion() {
        // Arrange
        partidaProgramada.setEstado("CANCELADA");
        when(repository.findById(1L)).thenReturn(Optional.of(partidaProgramada));

        PartidaUpdate update = new PartidaUpdate(null, null, null, "EN_CURSO");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            partidaService.actualizar(1L, update);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No iniciar partida cancelada", exception.getReason());
    }

    @Test
    void cancelar_PartidaExistente_DebeCambiarEstadoACancelada() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(partidaProgramada));
        when(repository.save(any(Partida.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Partida resultado = partidaService.cancelar(1L);

        // Assert
        assertEquals("CANCELADA", resultado.getEstado());
        verify(repository, times(1)).save(partidaProgramada);
    }
}