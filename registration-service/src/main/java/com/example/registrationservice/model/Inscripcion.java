package com.example.registrationservice.model;

import java.time.LocalDateTime;

public class Inscripcion {
    private Long id;
    private Long torneoId;
    private Long equipoId;
    private Long jugadorId;
    private String tipoParticipante;
    private String estado = "ACEPTADA";
    private LocalDateTime fechaInscripcion = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public Long getJugadorId() { return jugadorId; }
    public void setJugadorId(Long jugadorId) { this.jugadorId = jugadorId; }
    public String getTipoParticipante() { return tipoParticipante; }
    public void setTipoParticipante(String tipoParticipante) { this.tipoParticipante = tipoParticipante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}
