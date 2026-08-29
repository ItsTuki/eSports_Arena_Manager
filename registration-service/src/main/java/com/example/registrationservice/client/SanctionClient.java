package com.example.registrationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "sanction-service", url = "${services.sanction.url}")
public interface SanctionClient {
    @GetMapping("/api/v1/sanciones/bloqueo")
    Map<String, Boolean> bloqueo(@RequestParam(required = false) Long usuarioId,
                                 @RequestParam(required = false) Long equipoId);
}
