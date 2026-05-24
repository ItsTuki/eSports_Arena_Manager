package com.example.teamservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Long capitanId;
    @Column(nullable = false)
    private Long juegoPrincipalId;
    @Column(nullable = false)
    private String estado = "ACTIVO";
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MiembroEquipo> integrantes = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getCapitanId() { return capitanId; }
    public void setCapitanId(Long capitanId) { this.capitanId = capitanId; }
    public Long getJuegoPrincipalId() { return juegoPrincipalId; }
    public void setJuegoPrincipalId(Long juegoPrincipalId) { this.juegoPrincipalId = juegoPrincipalId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<MiembroEquipo> getIntegrantes() { return integrantes; }
    public void setIntegrantes(List<MiembroEquipo> integrantes) {
        this.integrantes.clear();
        if (integrantes != null) this.integrantes.addAll(integrantes);
    }
}
