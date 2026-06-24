package com.example.teamservice.service;

import com.example.teamservice.client.GameClient;
import com.example.teamservice.client.GameClient.JuegoResponse;
import com.example.teamservice.client.UserClient;
import com.example.teamservice.dto.EquipoDtos.EquipoRequest;
import com.example.teamservice.dto.EquipoDtos.EquipoUpdate;
import com.example.teamservice.dto.EquipoDtos.MiembroRequest;
import com.example.teamservice.model.Equipo;
import com.example.teamservice.model.MiembroEquipo;
import com.example.teamservice.repository.EquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private EquipoRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private GameClient gameClient;

    @InjectMocks
    private EquipoService equipoService;

    private EquipoRequest equipoRequestValido;
    private Equipo equipoActivo;

    @BeforeEach
    void setUp() {
        // Datos de prueba para un request de creación
        List<MiembroRequest> integrantes = new ArrayList<>();
        integrantes.add(new MiembroRequest(2L, "JUGADOR"));

        equipoRequestValido = new EquipoRequest(
                "Los Vengadores",
                1L, // Capitan
                10L, // Juego
                integrantes
        );

        // Entidad de prueba
        equipoActivo = new Equipo();
        equipoActivo.setId(1L);
        equipoActivo.setNombre("Los Vengadores");
        equipoActivo.setCapitanId(1L);
        equipoActivo.setJuegoPrincipalId(10L);
        equipoActivo.setEstado("ACTIVO");

        MiembroEquipo capitan = new MiembroEquipo();
        capitan.setUsuarioId(1L);
        capitan.setRolDentroEquipo("CAPITAN");
        capitan.setEstado("ACTIVO");
        equipoActivo.getIntegrantes().add(capitan);
    }

    @Test
    void crear_EquipoValido_DebeGuardarYRetornarEquipo() {
        // Arrange
        // Simulamos que todos los usuarios consultados pueden competir
        when(userClient.puedeCompetir(anyLong())).thenReturn(Map.of("puedeCompetir", true));
        // Simulamos que el juego está activo
        when(gameClient.buscar(10L)).thenReturn(new JuegoResponse(10L, "ACTIVO"));
        when(repository.save(any(Equipo.class))).thenReturn(equipoActivo);

        // Act
        Equipo resultado = equipoService.crear(equipoRequestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("Los Vengadores", resultado.getNombre());
        verify(repository, times(1)).save(any(Equipo.class));
        verify(userClient, atLeastOnce()).puedeCompetir(anyLong());
        verify(gameClient, times(1)).buscar(10L);
    }

    @Test
    void crear_CapitanNoPuedeCompetir_DebeLanzarExcepcion() {
        // Arrange
        when(userClient.puedeCompetir(1L)).thenReturn(Map.of("puedeCompetir", false));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            equipoService.crear(equipoRequestValido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Usuario inactivo o sancionado no puede integrar equipo", exception.getReason());
        verify(repository, never()).save(any(Equipo.class));
    }

    @Test
    void crear_JuegoInactivo_DebeLanzarExcepcion() {
        // Arrange
        when(userClient.puedeCompetir(anyLong())).thenReturn(Map.of("puedeCompetir", true));
        when(gameClient.buscar(10L)).thenReturn(new JuegoResponse(10L, "INACTIVO"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            equipoService.crear(equipoRequestValido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Juego inactivo no permite equipos nuevos", exception.getReason());
    }

    @Test
    void crear_JugadorDuplicado_DebeLanzarExcepcion() {
        // Arrange
        when(userClient.puedeCompetir(anyLong())).thenReturn(Map.of("puedeCompetir", true));
        when(gameClient.buscar(10L)).thenReturn(new JuegoResponse(10L, "ACTIVO"));

        // Mandamos el mismo jugador dos veces en la lista
        List<MiembroRequest> integrantesDuplicados = Arrays.asList(
                new MiembroRequest(2L, "JUGADOR"),
                new MiembroRequest(2L, "SUPLENTE")
        );
        EquipoRequest requestInvalido = new EquipoRequest("Team", 1L, 10L, integrantesDuplicados);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            equipoService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Jugador duplicado"));
    }

    @Test
    void buscar_EquipoExistente_DebeRetornarEquipo() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(equipoActivo));

        // Act
        Equipo resultado = equipoService.buscar(1L);

        // Assert
        assertEquals(1L, resultado.getId());
    }

    @Test
    void agregarMiembro_AEquipoActivo_DebeAgregarYGuardar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(equipoActivo));
        when(userClient.puedeCompetir(3L)).thenReturn(Map.of("puedeCompetir", true));
        when(repository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MiembroRequest nuevoMiembro = new MiembroRequest(3L, "SOPORTE");

        // Act
        Equipo resultado = equipoService.agregarMiembro(1L, nuevoMiembro);

        // Assert
        // Originalmente tenía 1 capitán, ahora debe tener 2 miembros
        assertEquals(2, resultado.getIntegrantes().size());
        verify(repository, times(1)).save(equipoActivo);
    }

    @Test
    void agregarMiembro_AEquipoInactivo_DebeLanzarExcepcion() {
        // Arrange
        equipoActivo.setEstado("INACTIVO");
        when(repository.findById(1L)).thenReturn(Optional.of(equipoActivo));

        MiembroRequest nuevoMiembro = new MiembroRequest(3L, "SOPORTE");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            equipoService.agregarMiembro(1L, nuevoMiembro);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Equipo inactivo no permite agregar miembros", exception.getReason());
    }

    @Test
    void desactivar_EquipoActivo_DebeCambiarEstadoAInactivoACascada() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(equipoActivo));
        when(repository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Equipo resultado = equipoService.desactivar(1L);

        // Assert
        assertEquals("INACTIVO", resultado.getEstado());
        // Verificamos que también se desactivó el miembro (Capitán)
        assertEquals("INACTIVO", resultado.getIntegrantes().get(0).getEstado());
    }

    @Test
    void puedeInscribirse_EquipoValidoYMiembrosActivos_DebeRetornarTrue() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(equipoActivo));

        // Act
        boolean resultado = equipoService.puedeInscribirse(1L);

        // Assert
        assertTrue(resultado);
    }
}
