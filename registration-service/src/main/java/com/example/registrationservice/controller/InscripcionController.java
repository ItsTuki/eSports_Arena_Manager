package com.example.registrationservice.controller;

import com.example.registrationservice.dto.InscripcionDtos.EstadoRequest;
import com.example.registrationservice.dto.InscripcionDtos.InscripcionRequest;
import com.example.registrationservice.model.Inscripcion;
import com.example.registrationservice.service.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    private final InscripcionService service;
    public InscripcionController(InscripcionService service) { this.service = service; }
    @PostMapping public ResponseEntity<Inscripcion> crear(@Valid @RequestBody InscripcionRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Inscripcion> listar(@RequestParam(required = false) Long torneoId, @RequestParam(required = false) Long equipoId, @RequestParam(required = false) Long jugadorId) { return service.listar(torneoId, equipoId, jugadorId); }
    @GetMapping("/{id}") public Inscripcion buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}/estado") public Inscripcion estado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request) { return service.actualizarEstado(id, request); }
    @PatchMapping("/{id}/cancelar") public Inscripcion cancelar(@PathVariable Long id) { return service.cancelar(id); }
}
