package com.eSports.arena.registration.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa la inscripción de un jugador o equipo a un torneo.
 * Modelo de datos: Inscripcion(id, torneoId, equipoId, jugadorId,
 *                              tipoParticipante, estado, fechaInscripcion)
 */
@Entity
@Table(
        name = "inscripciones",
        uniqueConstraints = {
                // Regla: no duplicar inscripción del mismo participante en el mismo torneo
                @UniqueConstraint(
                        name = "uk_torneo_participante",
                        columnNames = {"torneo_id", "equipo_id", "jugador_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "torneo_id", nullable = false)
    private Long torneoId;

    /** Nulo cuando el tipo de participante es INDIVIDUAL */
    @Column(name = "equipo_id")
    private Long equipoId;

    /** Nulo cuando el tipo de participante es EQUIPO */
    @Column(name = "jugador_id")
    private Long jugadorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_participante", nullable = false, length = 20)
    private TipoParticipante tipoParticipante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoInscripcion estado = EstadoInscripcion.PENDIENTE;

    @Column(name = "fecha_inscripcion", nullable = false, updatable = false)
    private LocalDateTime fechaInscripcion;

    @PrePersist
    protected void onCreate() {
        this.fechaInscripcion = LocalDateTime.now();
    }

    // ── Enums internos ────────────────────────────────────────────────────────

    public enum TipoParticipante {
        INDIVIDUAL, EQUIPO
    }

    public enum EstadoInscripcion {
        PENDIENTE, CONFIRMADA, CANCELADA
    }
}