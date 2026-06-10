package com.example.rankingservice.controller;

import com.example.rankingservice.dto.RankingDtos.RankingRequest;
import com.example.rankingservice.dto.RankingDtos.RankingUpdate;
import com.example.rankingservice.model.Ranking;
import com.example.rankingservice.service.RankingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
@Tag(name = "Rankings", description = "Tabla de posiciones, puntos y estadisticas")
public class RankingController {
    private final RankingService service;
    public RankingController(RankingService service) { this.service = service; }
    @PostMapping public ResponseEntity<Ranking> crear(@Valid @RequestBody RankingRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Ranking> listar(@RequestParam(required = false) Long torneoId) { return service.listar(torneoId); }
    @GetMapping("/posicion") public Ranking posicion(@RequestParam Long torneoId, @RequestParam Long participanteId) { return service.posicion(torneoId, participanteId); }
    @PutMapping("/{id}") public Ranking actualizar(@PathVariable Long id, @Valid @RequestBody RankingUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/torneos/{torneoId}/cerrar") public List<Ranking> cerrar(@PathVariable Long torneoId) { return service.cerrar(torneoId); }
    @PatchMapping("/torneos/{torneoId}/reiniciar") public List<Ranking> reiniciar(@PathVariable Long torneoId) { return service.reiniciar(torneoId); }
}
