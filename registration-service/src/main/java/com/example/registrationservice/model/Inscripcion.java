package com.registrationservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "torneo_id", nullable = false)
    private Long torneoId;

    @Column(name = "equipo_id")
    private Long equipoId; // Nullable si es inscripción INDIVIDUAL

    @Column(name = "jugador_id")
    private Long jugadorId; // Nullable si es inscripción por EQUIPO

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_participante", nullable = false, length = 30)
    private TipoParticipante tipoParticipante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoInscripcion estado;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @PrePersist
    protected void onCreate() {
        this.fechaInscripcion = LocalDateTime.now();
        if (this.estado == null) this.estado = EstadoInscripcion.PROCESANDO;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public Long getJugadorId() { return jugadorId; }
    public void setJugadorId(Long jugadorId) { this.jugadorId = jugadorId; }
    public TipoParticipante getTipoParticipante() { return tipoParticipante; }
    public void setTipoParticipante(TipoParticipante tipoParticipante) { this.tipoParticipante = tipoParticipante; }
    public EstadoInscripcion getEstado() { return estado; }
    public void setEstado(EstadoInscripcion estado) { this.estado = estado; }
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}