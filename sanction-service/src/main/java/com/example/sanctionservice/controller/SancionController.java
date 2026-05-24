package com.example.sanctionservice.controller;

import com.example.sanctionservice.dto.SancionDtos.SancionRequest;
import com.example.sanctionservice.dto.SancionDtos.SancionUpdate;
import com.example.sanctionservice.model.Sancion;
import com.example.sanctionservice.service.SancionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sanciones")
public class SancionController {
    private final SancionService service;
    public SancionController(SancionService service) { this.service = service; }
    @PostMapping public ResponseEntity<Sancion> crear(@Valid @RequestBody SancionRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Sancion> listar(@RequestParam(required = false) Long usuarioId, @RequestParam(required = false) Long equipoId, @RequestParam(required = false) String estado) { return service.listar(usuarioId, equipoId, estado); }
    @GetMapping("/{id}") public Sancion buscar(@PathVariable Long id) { return service.buscar(id); }
    @GetMapping("/bloqueo") public Map<String, Boolean> bloqueo(@RequestParam(required = false) Long usuarioId, @RequestParam(required = false) Long equipoId) { return Map.of("bloqueaInscripcion", service.bloqueaInscripcion(usuarioId, equipoId)); }
    @PutMapping("/{id}") public Sancion actualizar(@PathVariable Long id, @RequestBody SancionUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/cerrar") public Sancion cerrar(@PathVariable Long id) { return service.cerrar(id); }
}
