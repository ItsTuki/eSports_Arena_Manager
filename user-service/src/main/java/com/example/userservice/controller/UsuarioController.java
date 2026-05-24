package com.example.userservice.controller;

import com.example.userservice.dto.UsuarioDtos.UsuarioRequest;
import com.example.userservice.dto.UsuarioDtos.UsuarioUpdate;
import com.example.userservice.model.Usuario;
import com.example.userservice.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public List<Usuario> listar(@RequestParam(required = false) String rol,
                                @RequestParam(required = false) String nickname,
                                @RequestParam(required = false) String estado) {
        return service.listar(rol, nickname, estado);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/buscar")
    public Usuario buscarEmail(@RequestParam String email) {
        return service.buscarEmail(email);
    }

    @GetMapping("/{id}/puede-competir")
    public Map<String, Boolean> puedeCompetir(@PathVariable Long id) {
        return Map.of("puedeCompetir", service.puedeCompetir(id));
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody UsuarioUpdate request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/desactivar")
    public Usuario desactivar(@PathVariable Long id) {
        return service.desactivar(id);
    }

    @DeleteMapping("/{id}/desactivar")
    public Usuario desactivarDelete(@PathVariable Long id) {
        return service.desactivar(id);
    }
}
