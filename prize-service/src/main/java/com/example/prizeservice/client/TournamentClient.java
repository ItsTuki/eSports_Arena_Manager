package com.example.prizeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tournament-service", url = "${services.tournament.url}")
public interface TournamentClient {
    @GetMapping("/api/v1/torneos/{id}")
    TorneoResponse buscar(@PathVariable Long id);

    record TorneoResponse(Long id, String estado) {}
}
