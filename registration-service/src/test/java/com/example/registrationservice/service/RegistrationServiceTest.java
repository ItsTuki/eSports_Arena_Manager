package com.example.registrationservice.service;

import com.example.registrationservice.client.SanctionClient;
import com.example.registrationservice.client.TeamClient;
import com.example.registrationservice.client.TournamentClient;
import com.example.registrationservice.client.TournamentClient.TorneoResponse;
import com.example.registrationservice.client.UserClient;
import com.example.registrationservice.dto.InscripcionDtos.EstadoRequest;
import com.example.registrationservice.dto.InscripcionDtos.InscripcionRequest;
import com.example.registrationservice.model.Inscripcion;
import com.example.registrationservice.repository.InscripcionRepository;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private InscripcionRepository repository;

    @Mock
    private TournamentClient tournamentClient;

    @Mock
    private TeamClient teamClient;

    @Mock
    private UserClient userClient;

    @Mock
    private SanctionClient sanctionClient;

    @InjectMocks
    private InscripcionService inscripcionService;

    private TorneoResponse torneoAbierto;
    private Inscripcion inscripcionIndividual;
    private Inscripcion inscripcionEquipo;

    @BeforeEach
    void setUp() {
        // Torneo de prueba válido
        torneoAbierto = new TorneoResponse(
                1L,
                LocalDateTime.now().plusDays(5), // Fecha de cierre en el futuro
                100, // Cupo máximo
                "ABIERTO" // Estado
        );

        inscripcionIndividual = new Inscripcion();
        inscripcionIndividual.setId(1L);
        inscripcionIndividual.setTorneoId(1L);
        inscripcionIndividual.setJugadorId(10L);
        inscripcionIndividual.setTipoParticipante("INDIVIDUAL");
        inscripcionIndividual.setEstado("ACEPTADA");

        inscripcionEquipo = new Inscripcion();
        inscripcionEquipo.setId(2L);
        inscripcionEquipo.setTorneoId(1L);
        inscripcionEquipo.setEquipoId(20L);
        inscripcionEquipo.setTipoParticipante("EQUIPO");
        inscripcionEquipo.setEstado("ACEPTADA");
    }

    @Test
    void crear_InscripcionIndividualValida_DebeGuardarYRetornar() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(
                1L, null, 10L, "INDIVIDUAL", false, true, 100, LocalDateTime.now().plusDays(5)
        );

        when(tournamentClient.buscar(1L)).thenReturn(torneoAbierto);
        when(repository.findAll()).thenReturn(Collections.emptyList()); // Sin ocupados ni duplicados
        when(userClient.puedeCompetir(10L)).thenReturn(Map.of("puedeCompetir", true));
        when(sanctionClient.bloqueo(10L, null)).thenReturn(Map.of("bloqueaInscripcion", false));
        when(repository.save(any(Inscripcion.class))).thenReturn(inscripcionIndividual);

        // Act
        Inscripcion resultado = inscripcionService.crear(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("INDIVIDUAL", resultado.getTipoParticipante());
        assertEquals(10L, resultado.getJugadorId());
        verify(repository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    void crear_InscripcionEquipoValida_DebeGuardarYRetornar() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(
                1L, 20L, null, "EQUIPO", false, true, 100, LocalDateTime.now().plusDays(5)
        );

        when(tournamentClient.buscar(1L)).thenReturn(torneoAbierto);
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(teamClient.puedeInscribirse(20L)).thenReturn(Map.of("puedeInscribirse", true));
        when(sanctionClient.bloqueo(null, 20L)).thenReturn(Map.of("bloqueaInscripcion", false));
        when(repository.save(any(Inscripcion.class))).thenReturn(inscripcionEquipo);

        // Act
        Inscripcion resultado = inscripcionService.crear(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("EQUIPO", resultado.getTipoParticipante());
        assertEquals(20L, resultado.getEquipoId());
    }

    @Test
    void crear_FueraDePlazo_DebeLanzarExcepcion() {
        // Arrange
        TorneoResponse torneoCerrado = new TorneoResponse(1L, LocalDateTime.now().minusDays(1), 100, "CERRADO");
        when(tournamentClient.buscar(1L)).thenReturn(torneoCerrado);

        InscripcionRequest request = new InscripcionRequest(
                1L, null, 10L, "INDIVIDUAL", false, null, null, null
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            inscripcionService.crear(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No inscribir fuera de plazo", exception.getReason());
    }

    @Test
    void crear_CupoLleno_DebeLanzarExcepcion() {
        // Arrange
        TorneoResponse torneoPequeño = new TorneoResponse(1L, LocalDateTime.now().plusDays(5), 1, "ABIERTO");
        when(tournamentClient.buscar(1L)).thenReturn(torneoPequeño);

        // Ya hay 1 inscripción activa
        when(repository.findAll()).thenReturn(List.of(inscripcionIndividual));

        InscripcionRequest request = new InscripcionRequest(
                1L, null, 11L, "INDIVIDUAL", false, null, null, null
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            inscripcionService.crear(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No superar cupos", exception.getReason());
    }

    @Test
    void crear_ParticipanteConSancionBloqueante_DebeLanzarExcepcion() {
        // Arrange
        when(tournamentClient.buscar(1L)).thenReturn(torneoAbierto);
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(userClient.puedeCompetir(10L)).thenReturn(Map.of("puedeCompetir", true));

        // Simular que el cliente de sanciones devuelve un bloqueo
        when(sanctionClient.bloqueo(10L, null)).thenReturn(Map.of("bloqueaInscripcion", true));

        InscripcionRequest request = new InscripcionRequest(
                1L, null, 10L, "INDIVIDUAL", false, null, null, null
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            inscripcionService.crear(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Participante con sancion activa bloqueante", exception.getReason());
    }

    @Test
    void crear_InscripcionDuplicada_DebeLanzarExcepcion() {
        // Arrange
        when(tournamentClient.buscar(1L)).thenReturn(torneoAbierto);

        // El jugador 10L ya está inscrito
        when(repository.findAll()).thenReturn(List.of(inscripcionIndividual));
        when(userClient.puedeCompetir(10L)).thenReturn(Map.of("puedeCompetir", true));
        when(sanctionClient.bloqueo(10L, null)).thenReturn(Map.of("bloqueaInscripcion", false));

        InscripcionRequest request = new InscripcionRequest(
                1L, null, 10L, "INDIVIDUAL", false, null, null, null
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            inscripcionService.crear(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No duplicar inscripcion en el mismo torneo", exception.getReason());
    }

    @Test
    void listar_FiltroPorEquipo_DebeRetornarSoloElEquipoBuscado() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(inscripcionIndividual, inscripcionEquipo));

        // Act
        List<Inscripcion> resultados = inscripcionService.listar(1L, 20L, null);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals(20L, resultados.get(0).getEquipoId());
    }

    @Test
    void actualizarEstado_DebeModificarYGuardar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(inscripcionIndividual));
        when(repository.save(any(Inscripcion.class))).thenAnswer(i -> i.getArgument(0));

        EstadoRequest request = new EstadoRequest("RECHAZADA");

        // Act
        Inscripcion resultado = inscripcionService.actualizarEstado(1L, request);

        // Assert
        assertEquals("RECHAZADA", resultado.getEstado());
        verify(repository, times(1)).save(inscripcionIndividual);
    }

    @Test
    void cancelar_DebePonerEstadoEnCancelada() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(inscripcionIndividual));
        when(repository.save(any(Inscripcion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Inscripcion resultado = inscripcionService.cancelar(1L);

        // Assert
        assertEquals("CANCELADA", resultado.getEstado());
        verify(repository, times(1)).save(inscripcionIndividual);
    }
}
