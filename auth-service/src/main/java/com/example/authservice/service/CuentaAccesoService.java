package com.example.authservice.service;

import com.example.authservice.dto.AuthDtos.ActualizarCuentaRequest;
import com.example.authservice.dto.AuthDtos.CrearCuentaRequest;
import com.example.authservice.dto.AuthDtos.LoginRequest;
import com.example.authservice.client.UserClient;
import com.example.authservice.model.CuentaAcceso;
import com.example.authservice.repository.CuentaAccesoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CuentaAccesoService {
    private final CuentaAccesoRepository repository;
    private final UserClient userClient;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public CuentaAccesoService(CuentaAccesoRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    public CuentaAcceso crear(CrearCuentaRequest request) {
        validarRol(request.rol());
        userClient.buscarPorEmail(request.email());
        if (repository.existsByEmail(request.email())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo ya registrado");
        CuentaAcceso cuenta = new CuentaAcceso();
        cuenta.setEmail(request.email());
        cuenta.setPasswordHash(encoder.encode(request.password()));
        cuenta.setRol(request.rol().toUpperCase());
        cuenta.getHistorial().add("Cuenta creada " + cuenta.getFechaCreacion());
        return repository.save(cuenta);
    }

    public List<CuentaAcceso> listar(String rol, String estado) {
        return repository.findAll().stream()
                .filter(c -> rol == null || c.getRol().equalsIgnoreCase(rol))
                .filter(c -> estado == null || c.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public CuentaAcceso buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));
    }

    public CuentaAcceso buscarEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));
    }

    public CuentaAcceso actualizar(Long id, ActualizarCuentaRequest request) {
        CuentaAcceso cuenta = buscar(id);
        if (request.password() != null && !request.password().isBlank()) {
            cuenta.setPasswordHash(encoder.encode(request.password()));
            cuenta.getHistorial().add("Password actualizada " + LocalDateTime.now());
        }
        if (request.rol() != null) {
            validarRol(request.rol());
            cuenta.getHistorial().add("Rol cambiado de " + cuenta.getRol() + " a " + request.rol().toUpperCase());
            cuenta.setRol(request.rol().toUpperCase());
        }
        if (request.estado() != null) {
            validarEstado(request.estado());
            cuenta.getHistorial().add("Estado cambiado de " + cuenta.getEstado() + " a " + request.estado().toUpperCase());
            cuenta.setEstado(request.estado().toUpperCase());
        }
        return repository.save(cuenta);
    }

    public CuentaAcceso desactivar(Long id) {
        CuentaAcceso cuenta = buscar(id);
        cuenta.setEstado("DESACTIVADO");
        cuenta.getHistorial().add("Cuenta desactivada " + LocalDateTime.now());
        return repository.save(cuenta);
    }

    public CuentaAcceso autenticar(LoginRequest request) {
        CuentaAcceso cuenta = buscarEmail(request.email());
        if (!"ACTIVO".equals(cuenta.getEstado()) || !encoder.matches(request.password(), cuenta.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
        return cuenta;
    }

    private void validarRol(String rol) {
        if (!List.of("JUGADOR", "ORGANIZADOR", "ADMINISTRADOR").contains(rol.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido");
        }
    }

    private void validarEstado(String estado) {
        if (!List.of("ACTIVO", "DESACTIVADO").contains(estado.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado invalido");
        }
    }
}
