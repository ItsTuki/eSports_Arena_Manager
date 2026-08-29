package com.example.registrationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "team-service", url = "${services.team.url}")
public interface TeamClient {
    @GetMapping("/api/v1/equipos/{id}/puede-inscribirse")
    Map<String, Boolean> puedeInscribirse(@PathVariable Long id);
}
