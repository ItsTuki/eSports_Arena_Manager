package com.eSports.arena.registration.dto.request;

import com.eSports.arena.registration.model.Inscripcion.TipoParticipante;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para crear una nueva inscripción.
 * Bean Validation asegura que los campos obligatorios estén presentes.
 */
@Data
public class InscripcionRequest {

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long torneoId;

    /**
     * Obligatorio si tipoParticipante == EQUIPO.
     * La validación cruzada se realiza en la capa Service.
     */
    private Long equipoId;

    /**
     * Obligatorio si tipoParticipante == INDIVIDUAL.
     */
    private Long jugadorId;

    @NotNull(message = "El tipo de participante es obligatorio (INDIVIDUAL o EQUIPO)")
    private TipoParticipante tipoParticipante;
}