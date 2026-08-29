package com.example.tournamentservice.service;

import com.example.tournamentservice.client.GameClient;
import com.example.tournamentservice.client.GameClient.JuegoResponse;
import com.example.tournamentservice.dto.TorneoDtos.TorneoRequest;
import com.example.tournamentservice.dto.TorneoDtos.TorneoUpdate;
import com.example.tournamentservice.model.Torneo;
import com.example.tournamentservice.repository.TorneoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TournamentServiceTest {

    @Mock
    private TorneoRepository repository;

    @Mock
    private GameClient gameClient;

    @InjectMocks
    private TorneoService torneoService;

    private TorneoRequest torneoRequestValido;
    private Torneo torneo;

    @BeforeEach
    void setUp() {
        LocalDateTime cierre = LocalDateTime.now().plusDays(1);
        LocalDateTime inicio = LocalDateTime.now().plusDays(2);
        LocalDateTime fin = LocalDateTime.now().plusDays(3);

        torneoRequestValido = new TorneoRequest(
                "Torneo de Verano",
                1L,
                inicio,
                fin,
                cierre,
                100,
                "INDIVIDUAL",
                "BORRADOR"
        );

        torneo = new Torneo();
        torneo.setId(1L);
        torneo.setNombre("Torneo de Verano");
        torneo.setJuegoId(1L);
        torneo.setFechaInicio(inicio);
        torneo.setFechaFin(fin);
        torneo.setFechaCierreInscripcion(cierre);
        torneo.setCupoMaximo(100);
        torneo.setModalidad("INDIVIDUAL");
        torneo.setEstado("BORRADOR");
    }

    @Test
    void crear_TorneoValido_DebeRetornarTorneoCreado() {
        // Arrange
        when(gameClient.buscar(1L)).thenReturn(new JuegoResponse(1L, "ACTIVO"));
        when(repository.save(any(Torneo.class))).thenReturn(torneo);

        // Act
        Torneo resultado = torneoService.crear(torneoRequestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("Torneo de Verano", resultado.getNombre());
        verify(repository, times(1)).save(any(Torneo.class));
    }

    @Test
    void crear_JuegoInactivo_DebeLanzarExcepcion() {
        // Arrange
        when(gameClient.buscar(1L)).thenReturn(new JuegoResponse(1L, "MANTENIMIENTO"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            torneoService.crear(torneoRequestValido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Juego inactivo no permite nuevos torneos", exception.getReason());
        verify(repository, never()).save(any(Torneo.class));
    }

    @Test
    void crear_FechasInvalidasInicioAntesDeCierre_DebeLanzarExcepcion() {
        // Arrange
        when(gameClient.buscar(1L)).thenReturn(new JuegoResponse(1L, "ACTIVO"));

        TorneoRequest requestInvalido = new TorneoRequest(
                "Torneo", 1L,
                LocalDateTime.now(), // Inicio
                LocalDateTime.now().plusDays(2), // Fin
                LocalDateTime.now().plusDays(1), // Cierre posterior al inicio
                100, "INDIVIDUAL", "BORRADOR"
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            torneoService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Fecha de inicio debe ser posterior al cierre"));
    }

    @Test
    void buscar_TorneoExistente_DebeRetornarTorneo() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(torneo));

        // Act
        Torneo resultado = torneoService.buscar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscar_TorneoNoExistente_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            torneoService.buscar(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Torneo no encontrado", exception.getReason());
    }

    @Test
    void listar_SinFiltros_DebeRetornarTodosLosTorneos() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(torneo));

        // Act
        List<Torneo> resultados = torneoService.listar(null, null, null);

        // Assert
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
    }

    @Test
    void actualizar_TorneoEnBorrador_DebeActualizarDatos() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(torneo));
        when(repository.save(any(Torneo.class))).thenReturn(torneo);

        TorneoUpdate update = new TorneoUpdate(
                null, null, null, 200, "ABIERTO"
        );

        // Act
        Torneo resultado = torneoService.actualizar(1L, update);

        // Assert
        assertNotNull(resultado);
        assertEquals(200, resultado.getCupoMaximo()); // Verifica que el cupo se actualizó a 200
        assertEquals("ABIERTO", resultado.getEstado());
    }

    @Test
    void actualizar_TorneoEnCurso_DebeLanzarExcepcion() {
        // Arrange
        torneo.setEstado("EN_CURSO");
        when(repository.findById(1L)).thenReturn(Optional.of(torneo));

        TorneoUpdate update = new TorneoUpdate(null, null, null, 200, "CERRADO");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            torneoService.actualizar(1L, update);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("No modificar reglas criticas"));
    }

    @Test
    void cerrar_TorneoExistente_DebeCambiarEstadoACerrado() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(torneo));
        when(repository.save(any(Torneo.class))).thenReturn(torneo);

        // Act
        Torneo resultado = torneoService.cerrar(1L);

        // Assert
        assertEquals("CERRADO", resultado.getEstado());
        verify(repository, times(1)).save(torneo);
    }

    @Test
    void cancelar_TorneoExistente_DebeCambiarEstadoACancelado() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(torneo));
        when(repository.save(any(Torneo.class))).thenReturn(torneo);

        // Act
        Torneo resultado = torneoService.cancelar(1L);

        // Assert
        assertEquals("CANCELADO", resultado.getEstado());
        verify(repository, times(1)).save(torneo);
    }
}