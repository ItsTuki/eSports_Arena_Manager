package com.example.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${services.user.url}")
public interface UserClient {
    @GetMapping("/api/v1/usuarios/buscar")
    Object buscarPorEmail(@RequestParam String email);
}
