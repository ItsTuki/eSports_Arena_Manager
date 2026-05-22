package com.teamservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Column(nullable = false)
    private Long capitanId; // Debe coincidir con el usuarioId que tenga el rol de CAPITAN

    @NotNull
    @Column(nullable = false)
    private Long juegoPrincipalId; // ID proveniente de game-service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEquipo estado;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MiembroEquipo> integrantes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.estado == null) this.estado = EstadoEquipo.ACTIVO;
    }

    // Helpers para sincronizar la relación bidireccional de manera segura
    public void agregarIntegrante(Long usuarioId, RolMiembro rol) {
        MiembroEquipo nuevo = new MiembroEquipo(usuarioId, rol, this);
        this.integrantes.add(nuevo);
    }

    public void limpiarIntegrantes() {
        this.integrantes.clear();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getCapitanId() { return capitanId; }
    public void setCapitanId(Long capitanId) { this.capitanId = capitanId; }
    public Long getJuegoPrincipalId() { return juegoPrincipalId; }
    public void setJuegoPrincipalId(Long juegoPrincipalId) { this.juegoPrincipalId = juegoPrincipalId; }
    public EstadoEquipo getEstado() { return estado; }
    public void setEstado(EstadoEquipo estado) { this.estado = estado; }
    public List<MiembroEquipo> getIntegrantes() { return integrantes; }
    public void setIntegrantes(List<MiembroEquipo> integrantes) { this.integrantes = integrantes; }
}