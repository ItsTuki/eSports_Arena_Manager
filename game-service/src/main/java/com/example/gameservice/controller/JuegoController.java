package com.example.gameservice.controller;

import com.example.gameservice.dto.JuegoDtos.JuegoRequest;
import com.example.gameservice.dto.JuegoDtos.JuegoUpdate;
import com.example.gameservice.model.Juego;
import com.example.gameservice.service.JuegoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/juegos")
@Tag(name = "Juegos", description = "Videojuegos habilitados para torneos")
public class JuegoController {
    private final JuegoService service;
    public JuegoController(JuegoService service) { this.service = service; }

    @PostMapping public ResponseEntity<Juego> crear(@Valid @RequestBody JuegoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }
    @GetMapping public List<Juego> activos(@RequestParam(required = false, defaultValue = "false") boolean todos) { return todos ? service.listarTodos() : service.activos(); }
    @GetMapping("/{id}") public Juego buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Juego actualizar(@PathVariable Long id, @Valid @RequestBody JuegoUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/desactivar") public Juego desactivar(@PathVariable Long id) { return service.desactivar(id); }
    @DeleteMapping("/{id}/desactivar") public Juego desactivarDelete(@PathVariable Long id) { return service.desactivar(id); }
}
