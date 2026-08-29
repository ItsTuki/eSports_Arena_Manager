package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class UsuarioDtos {
    private UsuarioDtos() {}

    public record UsuarioRequest(@NotBlank String nombre, @NotBlank String nickname, @Email @NotBlank String email,
                                 @NotBlank String rol, String estado) {}
    public record UsuarioUpdate(String nombre, String nickname, String email, String rol, String estado) {}
}
