package com.gameservice.controller;

import com.gameservice.dto.JuegoRequestDTO;
import com.gameservice.model.Juego;
import com.gameservice.model.Modalidad;
import com.gameservice.service.JuegoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    private final JuegoService service;

    public JuegoController(JuegoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Juego> crearJuego(@Valid @RequestBody JuegoRequestDTO dto) {
        Juego nuevo = service.crearJuego(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


    @GetMapping("/activos")
    public ResponseEntity<List<Juego>> listarJuegosActivos() {
        return ResponseEntity.ok(service.listarJuegosActivos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Juego> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarJuego(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Modalidad modalidad = null;
        if (updates.get("modalidad") != null) {
            modalidad = Modalidad.valueOf(updates.get("modalidad").toString().toUpperCase());
        }
        String reglas = (String) updates.get("reglasGenerales");

        Juego actualizado = service.actualizarJuego(id, modalidad, reglas);
        return ResponseEntity.ok(actualizado);
    }


    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarJuego(@PathVariable Long id) {
        service.desactivarJuego(id);
        return ResponseEntity.ok(Map.of("message", "Videojuego desactivado correctamente."));
    }
}