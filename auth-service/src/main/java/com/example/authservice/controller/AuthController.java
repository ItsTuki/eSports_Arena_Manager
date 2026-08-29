package com.example.authservice.controller;

import com.example.authservice.dto.AuthDtos.ActualizarCuentaRequest;
import com.example.authservice.dto.AuthDtos.CrearCuentaRequest;
import com.example.authservice.dto.AuthDtos.LoginRequest;
import com.example.authservice.dto.AuthDtos.TokenResponse;
import com.example.authservice.model.CuentaAcceso;
import com.example.authservice.security.JwtService;
import com.example.authservice.service.CuentaAccesoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Autenticacion, JWT y cuentas de acceso")
public class AuthController {
    private final CuentaAccesoService service;
    private final JwtService jwtService;

    public AuthController(CuentaAccesoService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        CuentaAcceso cuenta = service.autenticar(request);
        return new TokenResponse(jwtService.emitir(cuenta), cuenta.getRol(), cuenta.getEstado());
    }

    @PostMapping("/validar-token")
    public Map<String, Boolean> validate(@RequestParam String token) {
        if (!jwtService.validar(token)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido");
        return Map.of("valid", true);
    }

    @PostMapping("/registro")
    public ResponseEntity<CuentaAcceso> crear(@Valid @RequestBody CrearCuentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/cuentas")
    public List<CuentaAcceso> listar(@RequestParam(required = false) String rol, @RequestParam(required = false) String estado) {
        return service.listar(rol, estado);
    }

    @GetMapping("/cuentas/{id}")
    public CuentaAcceso buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/cuentas/buscar")
    public CuentaAcceso buscarEmail(@RequestParam String email) {
        return service.buscarEmail(email);
    }

    @PutMapping("/cuentas/{id}")
    public CuentaAcceso actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarCuentaRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/cuentas/{id}/desactivar")
    public CuentaAcceso desactivar(@PathVariable Long id) {
        return service.desactivar(id);
    }

    @DeleteMapping("/cuentas/{id}/desactivar")
    public CuentaAcceso desactivarDelete(@PathVariable Long id) {
        return service.desactivar(id);
    }
}
