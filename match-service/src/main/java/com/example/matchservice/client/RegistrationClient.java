package com.example.matchservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "registration-service", url = "${services.registration.url}")
public interface RegistrationClient {
    @GetMapping("/api/v1/inscripciones")
    List<InscripcionResponse> listar(@RequestParam Long torneoId);

    record InscripcionResponse(Long equipoId, Long jugadorId, String estado) {}
}
