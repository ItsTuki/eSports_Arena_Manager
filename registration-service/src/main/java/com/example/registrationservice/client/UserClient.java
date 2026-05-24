package com.example.registrationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-service", url = "${services.user.url}")
public interface UserClient {
    @GetMapping("/api/v1/usuarios/{id}/puede-competir")
    Map<String, Boolean> puedeCompetir(@PathVariable Long id);
}
