package com.gameservice.dto;

import com.gameservice.model.Modalidad;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JuegoRequestDTO {

    @NotBlank(message = "El nombre del juego es obligatorio.")
    private String nombre;

    @NotBlank(message = "El género del juego es obligatorio.")
    private String genero;

    @NotNull(message = "La modalidad (INDIVIDUAL o EQUIPOS) es obligatoria.")
    private Modalidad modalidad;

    @NotNull(message = "La cantidad de jugadores por equipo es obligatoria.")
    @Min(value = 1, message = "La cantidad de jugadores por equipo debe ser un número positivo (mínimo 1).") // Regla: Positivo
    private Integer jugadoresPorEquipo;

    private String reglasGenerales;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Modalidad getModalidad() { return modalidad; }
    public void setModalidad(Modalidad modalidad) { this.modalidad = modalidad; }
    public Integer getJugadoresPorEquipo() { return jugadoresPorEquipo; }
    public void setJugadoresPorEquipo(Integer jugadoresPorEquipo) { this.jugadoresPorEquipo = jugadoresPorEquipo; }
    public String getReglasGenerales() { return reglasGenerales; }
    public void setReglasGenerales(String reglasGenerales) { this.reglasGenerales = reglasGenerales; }
}