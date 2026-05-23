package com.registrationservice.dto;

import com.registrationservice.model.TipoParticipante;
import jakarta.validation.constraints.NotNull;

public class InscripcionRequestDTO {

    @NotNull(message = "El ID del torneo es mandatorio.")
    private Long torneoId;

    @NotNull(message = "El tipo de participante (INDIVIDUAL o EQUIPO) es requerido.")
    private TipoParticipante tipoParticipante;

    private Long equipoId; // Obligatorio solo si tipoParticipante == EQUIPO
    private Long jugadorId; // Obligatorio solo si tipoParticipante == INDIVIDUAL

    // Getters y Setters
    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }
    public TipoParticipante getTipoParticipante() { return tipoParticipante; }
    public void setTipoParticipante(TipoParticipante tipoParticipante) { this.tipoParticipante = tipoParticipante; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public Long getJugadorId() { return jugadorId; }
    public void setJugadorId(Long jugadorId) { this.jugadorId = jugadorId; }
}