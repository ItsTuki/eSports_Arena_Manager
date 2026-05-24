package com.example.prizeservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class PremioDtos {
    private PremioDtos() {}
    public record PremioRequest(@NotNull Long torneoId, @Min(1) int posicion, @NotBlank String descripcion, BigDecimal valor) {}
    public record PremioUpdate(String descripcion, BigDecimal valor, String estado, Boolean autorizado) {}
    public record AsignarRequest(@NotNull Long participanteId, Boolean torneoFinalizado, Boolean rankingValidado) {}
}
