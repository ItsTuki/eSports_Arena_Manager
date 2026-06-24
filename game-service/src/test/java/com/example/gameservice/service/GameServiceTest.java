package com.example.gameservice.service;

import com.example.gameservice.dto.JuegoDtos.JuegoRequest;
import com.example.gameservice.dto.JuegoDtos.JuegoUpdate;
import com.example.gameservice.model.Juego;
import com.example.gameservice.repository.JuegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private JuegoRepository repository;

    @InjectMocks
    private JuegoService juegoService;

    private JuegoRequest juegoRequest;
    private Juego juegoActivo;
    private Juego juegoInactivo;

    @BeforeEach
    void setUp() {
        juegoRequest = new JuegoRequest(
                "League of Legends",
                "MOBA",
                "5v5",
                5,
                "Reglas estándar de Riot Games"
        );

        juegoActivo = new Juego();
        juegoActivo.setId(1L);
        juegoActivo.setNombre("League of Legends");
        juegoActivo.setGenero("MOBA");
        juegoActivo.setModalidad("5V5");
        juegoActivo.setJugadoresPorEquipo(5);
        juegoActivo.setEstado("ACTIVO");
        juegoActivo.setReglasGenerales("Reglas estándar de Riot Games");

        juegoInactivo = new Juego();
        juegoInactivo.setId(2L);
        juegoInactivo.setNombre("Valorant");
        juegoInactivo.setGenero("FPS");
        juegoInactivo.setModalidad("5V5");
        juegoInactivo.setJugadoresPorEquipo(5);
        juegoInactivo.setEstado("INACTIVO");
    }

    @Test
    void crear_JuegoNuevo_DebeGuardarYRetornarJuego() {
        // Arrange
        when(repository.findByNombre(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Juego.class))).thenReturn(juegoActivo);

        // Act
        Juego resultado = juegoService.crear(juegoRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals("League of Legends", resultado.getNombre());
        verify(repository, times(1)).findByNombre("League of Legends");
        verify(repository, times(1)).save(any(Juego.class));
    }

    @Test
    void crear_NombreYaRegistrado_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findByNombre("League of Legends")).thenReturn(Optional.of(juegoActivo));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            juegoService.crear(juegoRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Nombre de juego ya registrado", exception.getReason());
        verify(repository, never()).save(any(Juego.class));
    }

    @Test
    void activos_DebeRetornarSoloJuegosConEstadoActivo() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(juegoActivo, juegoInactivo));

        // Act
        List<Juego> resultados = juegoService.activos();

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("ACTIVO", resultados.get(0).getEstado());
        assertEquals("League of Legends", resultados.get(0).getNombre());
    }

    @Test
    void listarTodos_DebeRetornarTodosLosJuegos() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(juegoActivo, juegoInactivo));

        // Act
        List<Juego> resultados = juegoService.listarTodos();

        // Assert
        assertEquals(2, resultados.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void buscar_JuegoExistente_DebeRetornarJuego() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(juegoActivo));

        // Act
        Juego resultado = juegoService.buscar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscar_JuegoNoExistente_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            juegoService.buscar(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Juego no encontrado", exception.getReason());
    }

    @Test
    void actualizar_ConDatosNuevos_DebeActualizarYGuardar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(juegoActivo));
        when(repository.save(any(Juego.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JuegoUpdate update = new JuegoUpdate("1V1", "Nuevas reglas", "MANTENIMIENTO");

        // Act
        Juego resultado = juegoService.actualizar(1L, update);

        // Assert
        assertEquals("1V1", resultado.getModalidad());
        assertEquals("Nuevas reglas", resultado.getReglasGenerales());
        assertEquals("MANTENIMIENTO", resultado.getEstado());
        verify(repository, times(1)).save(any(Juego.class));
    }

    @Test
    void desactivar_JuegoExistente_DebeCambiarEstadoAInactivo() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(juegoActivo));
        when(repository.save(any(Juego.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Juego resultado = juegoService.desactivar(1L);

        // Assert
        assertEquals("INACTIVO", resultado.getEstado());
        verify(repository, times(1)).save(juegoActivo);
    }
}