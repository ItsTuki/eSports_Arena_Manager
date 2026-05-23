package com.example.gameservice.model;

public class Juego {
    private Long id;
    private String nombre;
    private String genero;
    private String modalidad;
    private int jugadoresPorEquipo;
    private String estado = "ACTIVO";
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
