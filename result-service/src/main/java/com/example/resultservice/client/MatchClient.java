package com.example.resultservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "match-service", url = "${services.match.url}")
public interface MatchClient {
    @GetMapping("/api/v1/partidas/{id}")
    PartidaResponse buscar(@PathVariable Long id);

    record PartidaResponse(Long id, Long participanteAId, Long participanteBId, String estado) {}
}
