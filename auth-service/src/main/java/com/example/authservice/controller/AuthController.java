package com.authservice.controller;

import com.authservice.model.CuentaAcceso;
import com.authservice.model.Estado;
import com.authservice.model.Rol;
import com.authservice.security.JwtUtil;
import com.authservice.service.CuentaAccesoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CuentaAccesoService service;
    private final JwtUtil jwtUtil;

    public AuthController(CuentaAccesoService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        return service.verificarCredenciales(loginRequest.get("email"), loginRequest.get("password"))
                .map(cuenta -> {
                    String token = jwtUtil.generateToken(cuenta);
                    return ResponseEntity.ok(Map.of("token", token, "rol", cuenta.getRol()));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas")));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        if (jwtUtil.validateToken(token)) {
            return ResponseEntity.ok(jwtUtil.extractAllClaims(token));
        }
        return ResponseEntity.status(401).body("Token inválido o expirado");
    }


    @PostMapping("/cuentas")
    public ResponseEntity<?> crearCuenta(@RequestBody CuentaAcceso cuenta) {
        try {
            return ResponseEntity.ok(service.crearCuenta(cuenta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/cuentas")
    public ResponseEntity<List<CuentaAcceso>> listarCuentas(
            @RequestParam(required = false) Rol rol,
            @RequestParam(required = false) Estado estado) {
        return ResponseEntity.ok(service.listarCuentas(rol, estado));
    }


    @GetMapping("/cuentas/buscar")
    public ResponseEntity<?> buscarCuenta(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email) {
        if (id != null) {
            return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } else if (email != null) {
            return service.buscarPorEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().body("Debe proveer un 'id' o un 'email' para la búsqueda.");
    }


    @PutMapping("/cuentas/{id}")
    public ResponseEntity<?> actualizarCuenta(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        String nuevaPassword = (String) updates.get("password");
        Rol nuevoRol = updates.get("rol") != null ? Rol.valueOf((String) updates.get("rol")) : null;
        Estado nuevoEstado = updates.get("estado") != null ? Estado.valueOf((String) updates.get("estado")) : null;

        try {
            CuentaAcceso actualizada = service.actualizarCuenta(id, nuevaPassword, nuevoRol, nuevoEstado);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/cuentas/{id}/desactivar")
    public ResponseEntity<?> desactivarCuenta(@PathVariable Long id) {
        try {
            service.desactivarCuenta(id);
            return ResponseEntity.ok(Map.of("message", "Cuenta desactivada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}