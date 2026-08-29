package com.example.teamservice.controller;

import com.example.teamservice.dto.EquipoDtos.EquipoRequest;
import com.example.teamservice.dto.EquipoDtos.EquipoUpdate;
import com.example.teamservice.dto.EquipoDtos.MiembroRequest;
import com.example.teamservice.model.Equipo;
import com.example.teamservice.service.EquipoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/equipos")
@Tag(name = "Equipos", description = "Equipos, integrantes, capitan y validacion de inscripcion")
public class EquipoController {
    private final EquipoService service;
    public EquipoController(EquipoService service) { this.service = service; }

    @PostMapping public ResponseEntity<Equipo> crear(@Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
    @GetMapping public List<Equipo> listar(@RequestParam(required = false) Long juegoId,
                                           @RequestParam(required = false) Long capitanId,
                                           @RequestParam(required = false) String estado) {
        return service.listar(juegoId, capitanId, estado);
    }
    @GetMapping("/{id}") public Equipo buscar(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping("/{id}/miembros") public Equipo agregarMiembro(@PathVariable Long id, @Valid @RequestBody MiembroRequest request) { return service.agregarMiembro(id, request); }
    @GetMapping("/{id}/puede-inscribirse") public Map<String, Boolean> puede(@PathVariable Long id) { return Map.of("puedeInscribirse", service.puedeInscribirse(id)); }
    @PutMapping("/{id}") public Equipo actualizar(@PathVariable Long id, @Valid @RequestBody EquipoUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/desactivar") public Equipo desactivar(@PathVariable Long id) { return service.desactivar(id); }
    @DeleteMapping("/{id}/desactivar") public Equipo desactivarDelete(@PathVariable Long id) { return service.desactivar(id); }
}
