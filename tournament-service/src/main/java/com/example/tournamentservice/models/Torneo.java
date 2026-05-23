package com.example.tournamentservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "torneos")
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Column(nullable = false)
    private Long juegoId;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaCierreInscripcion;

    @Min(2)
    @Column(nullable = false)
    private int cupoMaximo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTorneo estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadTorneo modalidad;

    @PrePersist
    protected void onCreate() {
        if (this.estado == null) this.estado = EstadoTorneo.BORRADOR;
    }

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
    public EstadoTorneo getEstado() { return estado; }
    public void setEstado(EstadoTorneo estado) { this.estado = estado; }
    public ModalidadTorneo getModalidad() { return modalidad; }
    public void setModalidad(ModalidadTorneo modalidad) { this.modalidad = modalidad; }
}
