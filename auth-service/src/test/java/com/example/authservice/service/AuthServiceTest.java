package com.example.authservice.service;

import com.example.authservice.client.UserClient;
import com.example.authservice.dto.AuthDtos.ActualizarCuentaRequest;
import com.example.authservice.dto.AuthDtos.CrearCuentaRequest;
import com.example.authservice.dto.AuthDtos.LoginRequest;
import com.example.authservice.model.CuentaAcceso;
import com.example.authservice.repository.CuentaAccesoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private CuentaAccesoRepository repository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private CuentaAccesoService cuentaAccesoService;

    private BCryptPasswordEncoder encoder;
    private CrearCuentaRequest requestCrear;
    private CuentaAcceso cuentaActiva;
    private CuentaAcceso cuentaDesactivada;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();

        requestCrear = new CrearCuentaRequest(
                "faker@esports.com",
                "Password123!",
                "JUGADOR"
        );

        cuentaActiva = new CuentaAcceso();
        cuentaActiva.setId(1L);
        cuentaActiva.setEmail("faker@esports.com");
        cuentaActiva.setPasswordHash(encoder.encode("Password123!"));
        cuentaActiva.setRol("JUGADOR");
        cuentaActiva.setEstado("ACTIVO");

        cuentaDesactivada = new CuentaAcceso();
        cuentaDesactivada.setId(2L);
        cuentaDesactivada.setEmail("baneado@esports.com");
        cuentaDesactivada.setPasswordHash(encoder.encode("Secret456"));
        cuentaDesactivada.setRol("JUGADOR");
        cuentaDesactivada.setEstado("DESACTIVADO");
    }

    @Test
    void crear_CuentaValida_DebeHashearPasswordYGuardar() {
        // Arrange
        when(userClient.buscarPorEmail(anyString())).thenReturn(new Object());
        when(repository.existsByEmail("faker@esports.com")).thenReturn(false);
        when(repository.save(any(CuentaAcceso.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CuentaAcceso resultado = cuentaAccesoService.crear(requestCrear);

        // Assert
        assertNotNull(resultado);
        assertEquals("faker@esports.com", resultado.getEmail());
        assertEquals("JUGADOR", resultado.getRol());
        assertTrue(encoder.matches("Password123!", resultado.getPasswordHash()));
        assertFalse(resultado.getHistorial().isEmpty()); // Verifica que se haya guardado el log de creación
        verify(repository, times(1)).save(any(CuentaAcceso.class));
    }

    @Test
    void crear_RolInvalido_DebeLanzarExcepcion() {
        // Arrange
        CrearCuentaRequest requestInvalido = new CrearCuentaRequest(
                "admin@esports.com", "pass", "SUPER_ADMIN"
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cuentaAccesoService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Rol invalido", exception.getReason());
        verify(repository, never()).save(any());
    }

    @Test
    void crear_EmailYaRegistrado_DebeLanzarExcepcion() {
        // Arrange
        when(userClient.buscarPorEmail(anyString())).thenReturn(new Object());
        when(repository.existsByEmail("faker@esports.com")).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cuentaAccesoService.crear(requestCrear);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Correo ya registrado", exception.getReason());
    }

    @Test
    void autenticar_CredencialesValidas_DebeRetornarCuenta() {
        // Arrange
        when(repository.findByEmail("faker@esports.com")).thenReturn(Optional.of(cuentaActiva));
        LoginRequest login = new LoginRequest("faker@esports.com", "Password123!");

        // Act
        CuentaAcceso resultado = cuentaAccesoService.autenticar(login);

        // Assert
        assertNotNull(resultado);
        assertEquals("faker@esports.com", resultado.getEmail());
    }

    @Test
    void autenticar_PasswordIncorrecto_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findByEmail("faker@esports.com")).thenReturn(Optional.of(cuentaActiva));
        LoginRequest login = new LoginRequest("faker@esports.com", "ClaveEquivocada");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cuentaAccesoService.autenticar(login);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Credenciales invalidas", exception.getReason());
    }

    @Test
    void autenticar_CuentaDesactivada_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findByEmail("baneado@esports.com")).thenReturn(Optional.of(cuentaDesactivada));
        LoginRequest login = new LoginRequest("baneado@esports.com", "Secret456");

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cuentaAccesoService.autenticar(login);
        });

        // Aunque la clave es correcta, el estado es DESACTIVADO
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Credenciales invalidas", exception.getReason());
    }

    @Test
    void actualizar_DatosNuevos_DebeHashearYRegistrarEnHistorial() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(cuentaActiva));
        when(repository.save(any(CuentaAcceso.class))).thenAnswer(i -> i.getArgument(0));

        ActualizarCuentaRequest update = new ActualizarCuentaRequest(
                "NuevaClaveSegura!", "ORGANIZADOR", null
        );

        // Act
        CuentaAcceso resultado = cuentaAccesoService.actualizar(1L, update);

        // Assert
        assertEquals("ORGANIZADOR", resultado.getRol());
        assertTrue(encoder.matches("NuevaClaveSegura!", resultado.getPasswordHash()));
        assertTrue(resultado.getHistorial().stream().anyMatch(h -> h.contains("Rol cambiado")));
        assertTrue(resultado.getHistorial().stream().anyMatch(h -> h.contains("Password actualizada")));
        verify(repository, times(1)).save(cuentaActiva);
    }

    @Test
    void desactivar_CuentaExistente_DebeCambiarEstadoYRegistrarEnHistorial() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(cuentaActiva));
        when(repository.save(any(CuentaAcceso.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CuentaAcceso resultado = cuentaAccesoService.desactivar(1L);

        // Assert
        assertEquals("DESACTIVADO", resultado.getEstado());
        assertTrue(resultado.getHistorial().stream().anyMatch(h -> h.contains("Cuenta desactivada")));
        verify(repository, times(1)).save(cuentaActiva);
    }

    @Test
    void listar_FiltroDoble_DebeRetornarCorrectamente() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(cuentaActiva, cuentaDesactivada));

        // Act
        List<CuentaAcceso> resultados = cuentaAccesoService.listar("JUGADOR", "ACTIVO");

        // Assert
        assertEquals(1, resultados.size());
        assertEquals(1L, resultados.get(0).getId());
    }
}