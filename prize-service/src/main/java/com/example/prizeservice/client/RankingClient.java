package com.example.prizeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ranking-service", url = "${services.ranking.url}")
public interface RankingClient {
    @GetMapping("/api/v1/rankings")
    List<RankingResponse> listar(@RequestParam Long torneoId);

    record RankingResponse(Long id, Long participanteId, int posicion, boolean cerrado) {}
}
