package com.example.sanctionservice.service;

import com.example.sanctionservice.client.TeamClient;
import com.example.sanctionservice.client.UserClient;
import com.example.sanctionservice.dto.SancionDtos.SancionRequest;
import com.example.sanctionservice.dto.SancionDtos.SancionUpdate;
import com.example.sanctionservice.model.Sancion;
import com.example.sanctionservice.repository.SancionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SanctionServiceTest {

    @Mock
    private SancionRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private TeamClient teamClient;

    @InjectMocks
    private SancionService sancionService;

    private SancionRequest requestUsuario;
    private Sancion sancionActiva;
    private Sancion sancionExpirada;

    @BeforeEach
    void setUp() {
        requestUsuario = new SancionRequest(
                10L, // usuarioId
                null, // equipoId
                "Uso de hacks",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "BLOQUEANTE"
        );

        sancionActiva = new Sancion();
        sancionActiva.setId(1L);
        sancionActiva.setUsuarioId(10L);
        sancionActiva.setMotivo("Uso de hacks");
        sancionActiva.setFechaInicio(LocalDate.now().minusDays(5));
        sancionActiva.setFechaFin(LocalDate.now().plusDays(25));
        sancionActiva.setEstado("ACTIVA");
        sancionActiva.setSeveridad("BLOQUEANTE");

        sancionExpirada = new Sancion();
        sancionExpirada.setId(2L);
        sancionExpirada.setEquipoId(20L);
        sancionExpirada.setMotivo("Ausencia injustificada");
        sancionExpirada.setFechaInicio(LocalDate.now().minusDays(20));
        sancionExpirada.setFechaFin(LocalDate.now().minusDays(1)); // Ya terminó ayer
        sancionExpirada.setEstado("ACTIVA");
        sancionExpirada.setSeveridad("BLOQUEANTE");
    }

    @Test
    void crear_SancionAUsuarioValida_DebeGuardarYRetornar() {
        // Arrange
        when(userClient.buscar(10L)).thenReturn(new Object());
        when(repository.save(any(Sancion.class))).thenReturn(sancionActiva);

        // Act
        Sancion resultado = sancionService.crear(requestUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("BLOQUEANTE", resultado.getSeveridad());
        assertEquals(10L, resultado.getUsuarioId());
        verify(userClient, times(1)).buscar(10L);
        verify(repository, times(1)).save(any(Sancion.class));
    }

    @Test
    void crear_SinDestinatario_DebeLanzarExcepcion() {
        // Arrange
        SancionRequest requestInvalido = new SancionRequest(
                null, null, "Motivo", LocalDate.now(), LocalDate.now().plusDays(1), "LEVE"
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sancionService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Sancion requiere usuario o equipo", exception.getReason());
        verify(repository, never()).save(any());
    }

    @Test
    void crear_FechasInvalidas_DebeLanzarExcepcion() {
        // Arrange
        // La fecha de fin es anterior a la de inicio
        SancionRequest requestInvalido = new SancionRequest(
                10L, null, "Motivo", LocalDate.now(), LocalDate.now().minusDays(1), "LEVE"
        );

        when(userClient.buscar(10L)).thenReturn(new Object());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            sancionService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Fecha fin debe ser posterior a fecha inicio", exception.getReason());
    }

    @Test
    void listar_FiltradoPorEstado_DebeRetornarCorrectamente() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(sancionActiva, sancionExpirada));

        // Act
        List<Sancion> resultados = sancionService.listar(null, null, "ACTIVA");

        // Assert
        assertEquals(2, resultados.size());
    }

    @Test
    void actualizar_DatosValidos_DebeActualizarYGuardar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(sancionActiva));
        when(repository.save(any(Sancion.class))).thenAnswer(i -> i.getArgument(0));

        SancionUpdate update = new SancionUpdate(
                "Apelación aceptada, se reduce sanción",
                null,
                LocalDate.now().plusDays(5), // Reducimos el tiempo
                null,
                "LEVE"
        );

        // Act
        Sancion resultado = sancionService.actualizar(1L, update);

        // Assert
        assertEquals("LEVE", resultado.getSeveridad());
        assertEquals("Apelación aceptada, se reduce sanción", resultado.getMotivo());
        assertEquals(LocalDate.now().plusDays(5), resultado.getFechaFin());
        verify(repository, times(1)).save(sancionActiva);
    }

    @Test
    void cerrar_SancionExistente_DebeCambiarEstadoACerrada() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(sancionActiva));
        when(repository.save(any(Sancion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Sancion resultado = sancionService.cerrar(1L);

        // Assert
        assertEquals("CERRADA", resultado.getEstado());
        verify(repository, times(1)).save(sancionActiva);
    }

    @Test
    void bloqueaInscripcion_SancionActivaYVigente_DebeRetornarTrue() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(sancionActiva));

        // Act
        boolean resultado = sancionService.bloqueaInscripcion(10L, null);

        // Assert
        assertTrue(resultado); // Porque la fecha fin es en el futuro y es BLOQUEANTE
    }

    @Test
    void bloqueaInscripcion_SancionExpirada_DebeRetornarFalse() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(sancionExpirada));

        // Act
        boolean resultado = sancionService.bloqueaInscripcion(null, 20L);

        // Assert
        assertFalse(resultado); // Porque la fecha fin fue ayer, aunque siga diciendo "ACTIVA"
    }

    @Test
    void bloqueaInscripcion_SancionCerrada_DebeRetornarFalse() {
        // Arrange
        sancionActiva.setEstado("CERRADA");
        when(repository.findAll()).thenReturn(List.of(sancionActiva));

        // Act
        boolean resultado = sancionService.bloqueaInscripcion(10L, null);

        // Assert
        assertFalse(resultado); // No es estado ACTIVA
    }

    @Test
    void crear_SancionAEquipoValida_DebeGuardarYRetornar() {
        // Arrange
        // 1. Creamos un request que sí tenga equipoId pero no usuarioId
        SancionRequest requestEquipo = new SancionRequest(
                null, // usuarioId
                20L,  // equipoId
                "Abandono de partida",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                "LEVE"
        );

        // 2. Simulamos que el teamClient encuentra al equipo exitosamente
        when(teamClient.buscar(20L)).thenReturn(new Object());

        // 3. Simulamos el guardado
        when(repository.save(any(Sancion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Sancion resultado = sancionService.crear(requestEquipo);

        // Assert
        assertNotNull(resultado);
        assertEquals(20L, resultado.getEquipoId());
        assertNull(resultado.getUsuarioId());

        // 4. Verificamos que el teamClient efectivamente fue llamado 1 vez
        verify(teamClient, times(1)).buscar(20L);

        // 5. Verificamos que el userClient NUNCA fue llamado (porque usuarioId es null)
        verify(userClient, never()).buscar(anyLong());
    }
}
