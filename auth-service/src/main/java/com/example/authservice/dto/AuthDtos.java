package com.example.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class AuthDtos {
    private AuthDtos() {}

    public record CrearCuentaRequest(@Email @NotBlank String email, @NotBlank String password, @NotBlank String rol) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record ActualizarCuentaRequest(String password, String rol, String estado) {}
    public record TokenResponse(String token, String rol, String estado) {}
}
