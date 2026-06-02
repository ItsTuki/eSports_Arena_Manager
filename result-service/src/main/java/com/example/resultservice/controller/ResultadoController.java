package com.example.resultservice.controller;

import com.example.resultservice.dto.ResultadoDtos.AnularRequest;
import com.example.resultservice.dto.ResultadoDtos.ResultadoRequest;
import com.example.resultservice.dto.ResultadoDtos.ResultadoUpdate;
import com.example.resultservice.model.Resultado;
import com.example.resultservice.service.ResultadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/resultados")
@Tag(name = "Resultados", description = "Resultados de partidas, puntajes y validacion")
public class ResultadoController {
    private final ResultadoService service;
    public ResultadoController(ResultadoService service) { this.service = service; }
    @PostMapping public ResponseEntity<Resultado> crear(@Valid @RequestBody ResultadoRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Resultado> listar(@RequestParam(required = false) Long torneoId, @RequestParam(required = false) Long partidaId) { return service.listar(torneoId, partidaId); }
    @GetMapping("/{id}") public Resultado buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Resultado actualizar(@PathVariable Long id, @RequestBody ResultadoUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/anular") public Resultado anular(@PathVariable Long id, @Valid @RequestBody AnularRequest request) { return service.anular(id, request); }
}
