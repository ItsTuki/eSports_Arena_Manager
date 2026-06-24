package com.example.userservice.service;

import com.example.userservice.dto.UsuarioDtos.UsuarioRequest;
import com.example.userservice.dto.UsuarioDtos.UsuarioUpdate;
import com.example.userservice.model.Usuario;
import com.example.userservice.repository.UsuarioRepository;
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
public class UserServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequest usuarioRequestValido;
    private Usuario usuarioActivo;
    private Usuario usuarioInactivo;

    @BeforeEach
    void setUp() {
        usuarioRequestValido = new UsuarioRequest(
                "Juan Perez",
                "Faker",
                "faker@esports.com",
                "JUGADOR",
                "ACTIVO"
        );

        usuarioActivo = new Usuario();
        usuarioActivo.setId(1L);
        usuarioActivo.setNombre("Juan Perez");
        usuarioActivo.setNickname("Faker");
        usuarioActivo.setEmail("faker@esports.com");
        usuarioActivo.setRol("JUGADOR");
        usuarioActivo.setEstado("ACTIVO");

        usuarioInactivo = new Usuario();
        usuarioInactivo.setId(2L);
        usuarioInactivo.setNombre("Maria Gomez");
        usuarioInactivo.setNickname("MG_Organiza");
        usuarioInactivo.setEmail("maria@esports.com");
        usuarioInactivo.setRol("ORGANIZADOR");
        usuarioInactivo.setEstado("INACTIVO");
    }

    @Test
    void crear_UsuarioValido_DebeGuardarYRetornarUsuario() {
        // Arrange
        when(repository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenReturn(usuarioActivo);

        // Act
        Usuario resultado = usuarioService.crear(usuarioRequestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals("Faker", resultado.getNickname());
        assertEquals("JUGADOR", resultado.getRol());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crear_RolInvalido_DebeLanzarExcepcion() {
        // Arrange
        UsuarioRequest requestInvalido = new UsuarioRequest(
                "Juan", "Juanito", "juan@mail.com", "ESPECTADOR", "ACTIVO"
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Rol invalido", exception.getReason());
        verify(repository, never()).save(any(Usuario.class));
    }

    @Test
    void crear_NicknameYaExiste_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findByNickname("Faker")).thenReturn(Optional.of(usuarioActivo));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.crear(usuarioRequestValido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Nickname ya registrado", exception.getReason());
        verify(repository, never()).save(any());
    }

    @Test
    void listar_FiltrosMultiples_DebeRetornarListaFiltrada() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(usuarioActivo, usuarioInactivo));

        // Act - Filtrando solo por rol "JUGADOR"
        List<Usuario> resultados = usuarioService.listar("JUGADOR", null, null);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals("Faker", resultados.get(0).getNickname());
    }

    @Test
    void buscar_UsuarioExistente_DebeRetornarUsuario() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioActivo));

        // Act
        Usuario resultado = usuarioService.buscar(1L);

        // Assert
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscar_UsuarioNoExistente_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioService.buscar(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void actualizar_DatosValidosYNicknameNuevo_DebeActualizar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioActivo));
        when(repository.findByNickname("NuevoFaker")).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioUpdate update = new UsuarioUpdate(null, "NuevoFaker", null, null, null);

        // Act
        Usuario resultado = usuarioService.actualizar(1L, update);

        // Assert
        assertEquals("NuevoFaker", resultado.getNickname());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void desactivar_UsuarioExistente_DebeCambiarEstadoAInactivo() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioActivo));
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.desactivar(1L);

        // Assert
        assertEquals("INACTIVO", resultado.getEstado());
        verify(repository).save(usuarioActivo);
    }

    @Test
    void puedeCompetir_UsuarioActivo_DebeRetornarTrue() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioActivo));

        // Act
        boolean resultado = usuarioService.puedeCompetir(1L);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void puedeCompetir_UsuarioInactivo_DebeRetornarFalse() {
        // Arrange
        when(repository.findById(2L)).thenReturn(Optional.of(usuarioInactivo));

        // Act
        boolean resultado = usuarioService.puedeCompetir(2L);

        // Assert
        assertFalse(resultado);
    }
}
