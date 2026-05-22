package com.teamservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "miembros_equipo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"equipo_id", "usuarioId"}))
public class MiembroEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long usuarioId; // ID proveniente de user-service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolMiembro rolDentroEquipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    @JsonIgnore
    private Equipo equipo;

    // Constructores, Getters y Setters
    public MiembroEquipo() {}

    public MiembroEquipo(Long usuarioId, RolMiembro rolDentroEquipo, Equipo equipo) {
        this.usuarioId = usuarioId;
        this.rolDentroEquipo = rolDentroEquipo;
        this.equipo = equipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public RolMiembro getRolDentroEquipo() { return rolDentroEquipo; }
    public void setRolDentroEquipo(RolMiembro rolDentroEquipo) { this.rolDentroEquipo = rolDentroEquipo; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
}