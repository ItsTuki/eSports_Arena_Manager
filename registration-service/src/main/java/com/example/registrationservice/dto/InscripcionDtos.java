package com.example.registrationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public final class InscripcionDtos {
    private InscripcionDtos() {}
    public record InscripcionRequest(@NotNull Long torneoId, Long equipoId, Long jugadorId, @NotBlank String tipoParticipante,
                                     Boolean participanteSancionado, Boolean torneoAbierto, Integer cupoMaximo,
                                     LocalDateTime fechaCierreInscripcion) {}
    public record EstadoRequest(@NotBlank String estado) {}
}
