package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificacionDtos.NotificacionRequest;
import com.example.notificationservice.dto.NotificacionDtos.NotificacionUpdate;
import com.example.notificationservice.model.Notificacion;
import com.example.notificationservice.repository.NotificacionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService notificacionService;

    private NotificacionRequest requestValido;
    private Notificacion notificacionActiva;

    @BeforeEach
    void setUp() {
        requestValido = new NotificacionRequest(
                10L, // usuarioId
                null, // equipoId
                "SISTEMA", // tipo
                "Tu inscripción ha sido aprobada" // mensaje
        );

        notificacionActiva = new Notificacion();
        notificacionActiva.setId(1L);
        notificacionActiva.setUsuarioId(10L);
        notificacionActiva.setTipo("SISTEMA");
        notificacionActiva.setMensaje("Tu inscripción ha sido aprobada");
        notificacionActiva.setLeida(false);
        notificacionActiva.setEstado("ACTIVA");
    }

    @Test
    void crear_ConDestinatario_DebeGuardarYRetornar() {
        // Arrange
        when(repository.save(any(Notificacion.class))).thenReturn(notificacionActiva);

        // Act
        Notificacion resultado = notificacionService.crear(requestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getUsuarioId());
        assertEquals("SISTEMA", resultado.getTipo());
        verify(repository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void crear_SinDestinatario_DebeLanzarExcepcion() {
        // Arrange
        NotificacionRequest requestInvalido = new NotificacionRequest(
                null, null, "SISTEMA", "Mensaje sin destino"
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificacionService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No crear notificacion sin destinatario", exception.getReason());
        verify(repository, never()).save(any(Notificacion.class));
    }

    @Test
    void listar_FiltroPorUsuario_DebeRetornarListaFiltrada() {
        // Arrange
        Notificacion notificacionEquipo = new Notificacion();
        notificacionEquipo.setEquipoId(20L);

        when(repository.findAll()).thenReturn(Arrays.asList(notificacionActiva, notificacionEquipo));

        // Act - Filtramos por usuarioId = 10L
        List<Notificacion> resultados = notificacionService.listar(10L, null);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals(10L, resultados.get(0).getUsuarioId());
    }

    @Test
    void buscar_Existente_DebeRetornar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(notificacionActiva));

        // Act
        Notificacion resultado = notificacionService.buscar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscar_NoExistente_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificacionService.buscar(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Notificacion no encontrada", exception.getReason());
    }

    @Test
    void actualizar_MarcarComoLeida_DebeActualizarCampoYEstado() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(notificacionActiva));
        when(repository.save(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));

        // Le enviamos leida = true
        NotificacionUpdate update = new NotificacionUpdate(true, null);

        // Act
        Notificacion resultado = notificacionService.actualizar(1L, update);

        // Assert
        assertTrue(resultado.isLeida());
        assertEquals("LEIDA", resultado.getEstado()); // La regla de negocio dice que si leida es true, el estado cambia a LEIDA
        verify(repository, times(1)).save(notificacionActiva);
    }

    @Test
    void leer_DebeModificarEstadoALeida() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(notificacionActiva));
        when(repository.save(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Notificacion resultado = notificacionService.leer(1L);

        // Assert
        assertTrue(resultado.isLeida());
        assertEquals("LEIDA", resultado.getEstado());
        verify(repository, times(1)).save(notificacionActiva);
    }

    @Test
    void archivar_DebeModificarEstadoAArchivada() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(notificacionActiva));
        when(repository.save(any(Notificacion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Notificacion resultado = notificacionService.archivar(1L);

        // Assert
        assertEquals("ARCHIVADA", resultado.getEstado());
        verify(repository, times(1)).save(notificacionActiva);
    }
}