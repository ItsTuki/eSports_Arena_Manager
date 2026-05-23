package com.example.matchservice.model;

import java.time.LocalDateTime;

public class Partida {
    private Long id;
    private Long torneoId;
    private Long participanteAId;
    private Long participanteBId;
    private Integer ronda;
    private LocalDateTime fechaHora;
    private String estado = "PROGRAMADA";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }
    public Long getParticipanteAId() { return participanteAId; }
    public void setParticipanteAId(Long participanteAId) { this.participanteAId = participanteAId; }
    public Long getParticipanteBId() { return participanteBId; }
    public void setParticipanteBId(Long participanteBId) { this.participanteBId = participanteBId; }
    public Integer getRonda() { return ronda; }
    public void setRonda(Integer ronda) { this.ronda = ronda; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
