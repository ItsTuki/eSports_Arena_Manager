package com.example.gameservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "juegos")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false)
    private String genero;
    @Column(nullable = false)
    private String modalidad;
    @Column(nullable = false)
    private int jugadoresPorEquipo;
    @Column(nullable = false)
    private String estado = "ACTIVO";
    @Column(columnDefinition = "TEXT")
    private String reglasGenerales;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public int getJugadoresPorEquipo() { return jugadoresPorEquipo; }
    public void setJugadoresPorEquipo(int jugadoresPorEquipo) { this.jugadoresPorEquipo = jugadoresPorEquipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getReglasGenerales() { return reglasGenerales; }
    public void setReglasGenerales(String reglasGenerales) { this.reglasGenerales = reglasGenerales; }
}
