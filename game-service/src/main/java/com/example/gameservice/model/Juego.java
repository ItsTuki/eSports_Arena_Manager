package com.gameservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String genero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Modalidad modalidad;

    @Column(name = "jugadores_por_equipo", nullable = false)
    private Integer jugadoresPorEquipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoJuego estado;

    @Column(name = "reglas_generales", columnDefinition = "TEXT")
    private String reglasGenerales;

    @PrePersist
    protected void onCreate() {
        if (this.estado == null) this.estado = EstadoJuego.ACTIVO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Modalidad getModalidad() { return modalidad; }
    public void setModalidad(Modalidad modalidad) { this.modalidad = modalidad; }
    public Integer getJugadoresPorEquipo() { return jugadoresPorEquipo; }
    public void setJugadoresPorEquipo(Integer jugadoresPorEquipo) { this.jugadoresPorEquipo = jugadoresPorEquipo; }
    public EstadoJuego getEstado() { return estado; }
    public void setEstado(EstadoJuego estado) { this.estado = estado; }
    public String getReglasGenerales() { return reglasGenerales; }
    public void setReglasGenerales(String reglasGenerales) { this.reglasGenerales = reglasGenerales; }
}