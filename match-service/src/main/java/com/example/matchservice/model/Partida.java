package com.matchservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
    private String estado;
}