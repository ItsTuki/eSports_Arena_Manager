package com.example.teamservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.util.List;

public final class EquipoDtos {
    private EquipoDtos() {}
    public record MiembroRequest(@NotNull Long usuarioId, @NotBlank String rolDentroEquipo) {}
    public record EquipoRequest(@NotBlank String nombre, @NotNull Long capitanId, @NotNull Long juegoPrincipalId,
                                List<@Valid MiembroRequest> integrantes) {}
    public record EquipoUpdate(String nombre, Long capitanId, List<@Valid MiembroRequest> integrantes, String estado) {}
}
