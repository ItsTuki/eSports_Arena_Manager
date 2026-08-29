package com.example.rankingservice.dto;

import jakarta.validation.constraints.NotNull;

public final class RankingDtos {
    private RankingDtos() {}
    public record RankingRequest(@NotNull Long torneoId, @NotNull Long participanteId) {}
    public record RankingUpdate(Integer puntos, Integer victorias, Integer derrotas, Integer diferencia, Boolean resultadoValidado) {}
}
