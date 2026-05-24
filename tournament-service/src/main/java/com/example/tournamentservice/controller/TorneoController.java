package com.example.tournamentservice.controller;

import com.example.tournamentservice.dto.TorneoDtos.TorneoRequest;
import com.example.tournamentservice.dto.TorneoDtos.TorneoUpdate;
import com.example.tournamentservice.model.Torneo;
import com.example.tournamentservice.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/torneos")
public class TorneoController {
    private final TorneoService service;
    public TorneoController(TorneoService service) { this.service = service; }
    @PostMapping public ResponseEntity<Torneo> crear(@Valid @RequestBody TorneoRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Torneo> listar(@RequestParam(required = false) Long juegoId, @RequestParam(required = false) String estado, @RequestParam(required = false) LocalDate fecha) { return service.listar(juegoId, estado, fecha); }
    @GetMapping("/{id}") public Torneo buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Torneo actualizar(@PathVariable Long id, @RequestBody TorneoUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/estado") public Torneo estado(@PathVariable Long id, @RequestParam String nuevoEstado) { return service.actualizar(id, new TorneoUpdate(null, null, null, null, nuevoEstado)); }
    @PatchMapping("/{id}/cerrar") public Torneo cerrar(@PathVariable Long id) { return service.cerrar(id); }
    @PatchMapping("/{id}/cancelar") public Torneo cancelar(@PathVariable Long id) { return service.cancelar(id); }
    @DeleteMapping("/{id}/cancelar") public Torneo cancelarDelete(@PathVariable Long id) { return service.cancelar(id); }
}
