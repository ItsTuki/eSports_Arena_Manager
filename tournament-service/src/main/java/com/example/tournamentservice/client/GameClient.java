package com.example.tournamentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "game-service", url = "${services.game.url}")
public interface GameClient {
    @GetMapping("/api/v1/juegos/{id}")
    JuegoResponse buscar(@PathVariable Long id);

    record JuegoResponse(Long id, String estado) {}
}
