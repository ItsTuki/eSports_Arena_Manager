package com.example.userservice.clients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient
public interface UserClient {

    @GetMapping("/api/auth/cuentas/buscar")
    Object buscarCuentaPorId(@PathVariable Long id);
}
