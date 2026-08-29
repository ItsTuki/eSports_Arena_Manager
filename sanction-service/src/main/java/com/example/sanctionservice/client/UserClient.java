package com.example.sanctionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${services.user.url}")
public interface UserClient {
    @GetMapping("/api/v1/usuarios/{id}")
    Object buscar(@PathVariable Long id);
}
