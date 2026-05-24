package com.example.sanctionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public final class SancionDtos {
    private SancionDtos() {}
    public record SancionRequest(Long usuarioId, Long equipoId, @NotBlank String motivo, @NotNull LocalDate fechaInicio, @NotNull LocalDate fechaFin, @NotBlank String severidad) {}
    public record SancionUpdate(String motivo, LocalDate fechaInicio, LocalDate fechaFin, String estado, String severidad) {}
}
