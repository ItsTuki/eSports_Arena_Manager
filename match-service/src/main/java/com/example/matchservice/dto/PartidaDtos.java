package com.example.matchservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public final class PartidaDtos {
    private PartidaDtos() {}
    public record PartidaRequest(@NotNull Long torneoId, @NotNull Long participanteAId, @NotNull Long participanteBId,
                                 @NotNull Integer ronda, @NotNull LocalDateTime fechaHora,
                                 Boolean participanteAInscrito, Boolean participanteBInscrito) {}
    public record PartidaUpdate(LocalDateTime fechaHora, Long participanteAId, Long participanteBId, String estado) {}
}
