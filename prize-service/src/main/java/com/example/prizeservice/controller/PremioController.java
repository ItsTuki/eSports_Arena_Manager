package com.example.prizeservice.controller;

import com.example.prizeservice.dto.PremioDtos.AsignarRequest;
import com.example.prizeservice.dto.PremioDtos.PremioRequest;
import com.example.prizeservice.dto.PremioDtos.PremioUpdate;
import com.example.prizeservice.model.Premio;
import com.example.prizeservice.model.PremioAsignado;
import com.example.prizeservice.service.PremioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/premios")
@Tag(name = "Premios", description = "Premios por torneo, posicion y asignaciones")
public class PremioController {
    private final PremioService service;
    public PremioController(PremioService service) { this.service = service; }
    @PostMapping public ResponseEntity<Premio> crear(@Valid @RequestBody PremioRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Premio> listar(@RequestParam(required = false) Long torneoId, @RequestParam(required = false) Integer posicion) { return service.listar(torneoId, posicion); }
    @GetMapping("/{id}") public Premio buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Premio actualizar(@PathVariable Long id, @Valid @RequestBody PremioUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/desactivar") public Premio desactivar(@PathVariable Long id) { return service.desactivar(id); }
    @DeleteMapping("/{id}/desactivar") public Premio desactivarDelete(@PathVariable Long id) { return service.desactivar(id); }
    @PostMapping("/{id}/asignaciones") public PremioAsignado asignar(@PathVariable Long id, @Valid @RequestBody AsignarRequest request) { return service.asignar(id, request); }
}
