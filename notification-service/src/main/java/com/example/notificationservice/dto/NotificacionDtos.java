package com.example.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;

public final class NotificacionDtos {
    private NotificacionDtos() {}
    public record NotificacionRequest(Long usuarioId, Long equipoId, @NotBlank String tipo, @NotBlank String mensaje) {}
    public record NotificacionUpdate(Boolean leida, String estado) {}
}
