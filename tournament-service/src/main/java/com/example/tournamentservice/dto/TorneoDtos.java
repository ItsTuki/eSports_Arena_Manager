package com.example.tournamentservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public final class TorneoDtos {
    private TorneoDtos() {}
    public record TorneoRequest(@NotBlank String nombre, @NotNull Long juegoId, @NotNull LocalDateTime fechaInicio,
                                @NotNull LocalDateTime fechaFin, @JsonAlias("fechaFinInscripcion") @NotNull LocalDateTime fechaCierreInscripcion,
                                @Min(1) int cupoMaximo, @NotBlank String modalidad, String estado) {}
    public record TorneoUpdate(LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDateTime fechaCierreInscripcion,
                               Integer cupoMaximo, String estado) {}
}
