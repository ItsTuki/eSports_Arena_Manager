package com.example.teamservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "miembros_equipo")
public class MiembroEquipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    @JsonIgnore
    private Equipo equipo;
    @Column(nullable = false)
    private Long usuarioId;
    @Column(nullable = false)
    private String rolDentroEquipo;
    @Column(nullable = false, columnDefinition = "varchar(255) default 'ACTIVO'")
    private String estado = "ACTIVO";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getRolDentroEquipo() { return rolDentroEquipo; }
    public void setRolDentroEquipo(String rolDentroEquipo) { this.rolDentroEquipo = rolDentroEquipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @PrePersist
    public void prePersist() {
        if (estado == null) estado = "ACTIVO";
    }
}
