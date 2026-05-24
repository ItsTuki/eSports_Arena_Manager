package com.example.teamservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public final class EquipoDtos {
    private EquipoDtos() {}
    public record MiembroRequest(@NotNull Long usuarioId, @NotBlank String rolDentroEquipo) {}
    public record EquipoRequest(@NotBlank String nombre, @NotNull Long capitanId, @NotNull Long juegoPrincipalId,
                                List<MiembroRequest> integrantes) {}
    public record EquipoUpdate(String nombre, Long capitanId, List<MiembroRequest> integrantes, String estado) {}
}
