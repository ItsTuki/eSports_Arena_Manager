package com.example.sanctionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-service", url = "${services.team.url}")
public interface TeamClient {
    @GetMapping("/api/v1/equipos/{id}")
    Object buscar(@PathVariable Long id);
}
