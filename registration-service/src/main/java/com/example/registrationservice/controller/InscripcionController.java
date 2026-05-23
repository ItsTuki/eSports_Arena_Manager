package com.registrationservice.controller;

import com.registrationservice.dto.InscripcionRequestDTO;
import com.registrationservice.model.Inscripcion;
import com.registrationservice.model.EstadoInscripcion;
import com.registrationservice.service.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService service;

    public InscripcionController(InscripcionService service) {
        this.service = service;
    }

    // 1. Crear inscripción
    @PostMapping
    public ResponseEntity<Inscripcion> crearInscripcion(@Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearInscripcion(dto));
    }

    // 2. Listar inscripciones por torneo, equipo o jugador
    @GetMapping
    public ResponseEntity<List<Inscripcion>> listarInscripciones(
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) Long jugadorId) {
        return ResponseEntity.ok(service.listarInscripciones(torneoId, equipoId, jugadorId));
    }

    // 3. Buscar inscripción por ID
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Actualizar estado de inscripción
    @PutMapping("/{id}/estado")
    public ResponseEntity<Inscripcion> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoInscripcion estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    // 5. Cancelar inscripción
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarInscripcion(@PathVariable Long id) {
        service.cancelarInscripcion(id);
        return ResponseEntity.ok(Map.of("message", "Inscripción cancelada con éxito."));
    }
}