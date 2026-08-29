package com.example.userservice.service;

import com.example.userservice.dto.UsuarioDtos.UsuarioRequest;
import com.example.userservice.dto.UsuarioDtos.UsuarioUpdate;
import com.example.userservice.model.Usuario;
import com.example.userservice.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crear(UsuarioRequest request) {
        validarRol(request.rol());
        validarEstado(request.estado() == null ? "ACTIVO" : request.estado());
        validarNicknameUnico(null, request.nickname());
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setNickname(request.nickname());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol().toUpperCase());
        if (request.estado() != null) usuario.setEstado(request.estado().toUpperCase());
        return repository.save(usuario);
    }

    public List<Usuario> listar(String rol, String nickname, String estado) {
        return repository.findAll().stream()
                .filter(u -> rol == null || u.getRol().equalsIgnoreCase(rol))
                .filter(u -> nickname == null || u.getNickname().toLowerCase().contains(nickname.toLowerCase()))
                .filter(u -> estado == null || u.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario buscarEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario actualizar(Long id, UsuarioUpdate request) {
        Usuario usuario = buscar(id);
        if (request.nickname() != null && !request.nickname().equalsIgnoreCase(usuario.getNickname())) {
            validarNicknameUnico(id, request.nickname());
            usuario.setNickname(request.nickname());
        }
        if (request.nombre() != null) usuario.setNombre(request.nombre());
        if (request.email() != null) usuario.setEmail(request.email());
        if (request.rol() != null) {
            validarRol(request.rol());
            usuario.setRol(request.rol().toUpperCase());
        }
        if (request.estado() != null) {
            validarEstado(request.estado());
            usuario.setEstado(request.estado().toUpperCase());
        }
        return repository.save(usuario);
    }

    public Usuario desactivar(Long id) {
        Usuario usuario = buscar(id);
        usuario.setEstado("INACTIVO");
        return repository.save(usuario);
    }

    public boolean puedeCompetir(Long id) {
        return "ACTIVO".equals(buscar(id).getEstado());
    }

    private void validarNicknameUnico(Long idActual, String nickname) {
        repository.findByNickname(nickname)
                .filter(u -> !Objects.equals(u.getId(), idActual))
                .ifPresent(u -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nickname ya registrado"); });
    }

    private void validarRol(String rol) {
        if (!List.of("JUGADOR", "ORGANIZADOR", "ADMINISTRADOR").contains(rol.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido");
        }
    }

    private void validarEstado(String estado) {
        if (!List.of("ACTIVO", "INACTIVO", "SANCIONADO").contains(estado.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado invalido");
        }
    }
}
