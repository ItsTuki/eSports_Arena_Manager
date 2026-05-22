package com.teamservice.controller;

import com.teamservice.model.Equipo;
import com.teamservice.model.EstadoEquipo;
import com.teamservice.model.MiembroEquipo;
import com.teamservice.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoService service;

    public EquipoController(EquipoService service) {
        this.service = service;
    }

    // 1. Crear Equipo
    @PostMapping
    public ResponseEntity<?> crearEquipo(@Valid @RequestBody Equipo equipo) {
        try {
            Equipo nuevo = service.crearEquipo(equipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. Listar equipos por juego, capitán o estado
    @GetMapping
    public ResponseEntity<List<Equipo>> listarEquipos(
            @RequestParam(required = false) Long juegoId,
            @RequestParam(required = false) Long capitanId,
            @RequestParam(required = false) EstadoEquipo estado) {
        return ResponseEntity.ok(service.listarEquipos(juegoId, capitanId, estado));
    }

    // 3. Buscar equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Equipo> buscarEquipoPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Actualizar nombre, capitán o integrantes
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEquipo(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            String nuevoNombre = (String) updates.get("nombre");
            Long nuevoCapitanId = updates.get("capitanId") != null ? Long.valueOf(updates.get("capitanId").toString()) : null;

            // Re-mapear lista de integrantes si viene en el cuerpo del payload
            List<MiembroEquipo> nuevosIntegrantes = null;
            if (updates.get("integrantes") != null) {
                List<Map<String, Object>> rawList = (List<Map<String, Object>>) updates.get("integrantes");
                nuevosIntegrantes = rawList.stream().map(m -> {
                    MiembroEquipo miembro = new MiembroEquipo();
                    miembro.setUsuarioId(Long.valueOf(m.get("usuarioId").toString()));
                    miembro.setRolDentroEquipo(RolMiembro.valueOf(m.get("rolDentroEquipo").toString()));
                    return miembro;
                }).toList();
            }

            Equipo actualizado = service.actualizarEquipo(id, nuevoNombre, nuevoCapitanId, nuevosIntegrantes);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // 5. Desactivar equipo
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarEquipo(@PathVariable Long id) {
        try {
            service.desactivarEquipo(id);
            return ResponseEntity.ok(Map.of("message", "Equipo desactivado correctamente."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}