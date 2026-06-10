package com.example.matchservice.controller;

import com.example.matchservice.dto.PartidaDtos.PartidaRequest;
import com.example.matchservice.dto.PartidaDtos.PartidaUpdate;
import com.example.matchservice.model.Partida;
import com.example.matchservice.service.PartidaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/partidas")
@Tag(name = "Partidas", description = "Partidas, rondas, horarios y participantes")
public class PartidaController {
    private final PartidaService service;
    public PartidaController(PartidaService service) { this.service = service; }
    @PostMapping public ResponseEntity<Partida> crear(@Valid @RequestBody PartidaRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Partida> listar(@RequestParam(required = false) Long torneoId, @RequestParam(required = false) Integer ronda, @RequestParam(required = false) String estado) { return service.listar(torneoId, ronda, estado); }
    @GetMapping("/{id}") public Partida buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Partida actualizar(@PathVariable Long id, @Valid @RequestBody PartidaUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}") public Partida actualizarPatch(@PathVariable Long id, @Valid @RequestBody PartidaUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/cancelar") public Partida cancelar(@PathVariable Long id) { return service.cancelar(id); }
    @DeleteMapping("/{id}/cancelar") public Partida cancelarDelete(@PathVariable Long id) { return service.cancelar(id); }
}
