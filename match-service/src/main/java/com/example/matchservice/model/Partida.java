package com.example.matchservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long torneoId;
    @Column(nullable = false)
    private Long participanteAId;
    @Column(nullable = false)
    private Long participanteBId;
    @Column(nullable = false)
    private Integer ronda;
    @Column(nullable = false)
    private LocalDateTime fechaHora;
    @Column(nullable = false)
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
