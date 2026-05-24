package com.example.tournamentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "torneos")
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Long juegoId;
    @Column(nullable = false)
    private LocalDateTime fechaInicio;
    @Column(nullable = false)
    private LocalDateTime fechaFin;
    @Column(nullable = false)
    private LocalDateTime fechaCierreInscripcion;
    @Column(nullable = false)
    private int cupoMaximo;
    @Column(nullable = false)
    private String estado = "BORRADOR";
    @Column(nullable = false)
    private String modalidad;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getJuegoId() { return juegoId; }
    public void setJuegoId(Long juegoId) { this.juegoId = juegoId; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public LocalDateTime getFechaCierreInscripcion() { return fechaCierreInscripcion; }
    public void setFechaCierreInscripcion(LocalDateTime fechaCierreInscripcion) { this.fechaCierreInscripcion = fechaCierreInscripcion; }
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
}
