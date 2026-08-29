package com.example.gameservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public final class JuegoDtos {
    private JuegoDtos() {}
    public record JuegoRequest(@NotBlank String nombre, @NotBlank String genero, @NotBlank String modalidad,
                               @Min(1) int jugadoresPorEquipo, String reglasGenerales) {}
    public record JuegoUpdate(String modalidad, String reglasGenerales, String estado) {}
}
