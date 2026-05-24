package com.example.prizeservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "premios_asignados")
public class PremioAsignado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long premioId;
    @Column(nullable = false)
    private Long participanteId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAsignacion = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPremioId() { return premioId; }
    public void setPremioId(Long premioId) { this.premioId = premioId; }
    public Long getParticipanteId() { return participanteId; }
    public void setParticipanteId(Long participanteId) { this.participanteId = participanteId; }
    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
}
