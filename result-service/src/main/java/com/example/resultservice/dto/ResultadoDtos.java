package com.example.resultservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class ResultadoDtos {
    private ResultadoDtos() {}
    public record ResultadoRequest(@NotNull Long partidaId, @NotNull Long ganadorId, @Min(0) int puntajeA, @Min(0) int puntajeB,
                                   String evidencia, Boolean partidaExiste) {}
    public record ResultadoUpdate(Long ganadorId, Integer puntajeA, Integer puntajeB, String evidencia, String estadoValidacion, Boolean rolOrganizador) {}
    public record AnularRequest(@NotBlank String justificacion) {}
}
