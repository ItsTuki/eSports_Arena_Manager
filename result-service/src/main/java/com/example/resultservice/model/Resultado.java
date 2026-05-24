package com.example.resultservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long partidaId;
    @Column(nullable = false)
    private Long ganadorId;
    @Column(nullable = false)
    private int puntajeA;
    @Column(nullable = false)
    private int puntajeB;
    @Column(columnDefinition = "TEXT")
    private String evidencia;
    @Column(nullable = false)
    private String estadoValidacion = "PENDIENTE";
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    @Column(columnDefinition = "TEXT")
    private String justificacionAnulacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPartidaId() { return partidaId; }
    public void setPartidaId(Long partidaId) { this.partidaId = partidaId; }
    public Long getGanadorId() { return ganadorId; }
    public void setGanadorId(Long ganadorId) { this.ganadorId = ganadorId; }
    public int getPuntajeA() { return puntajeA; }
    public void setPuntajeA(int puntajeA) { this.puntajeA = puntajeA; }
    public int getPuntajeB() { return puntajeB; }
    public void setPuntajeB(int puntajeB) { this.puntajeB = puntajeB; }
    public String getEvidencia() { return evidencia; }
    public void setEvidencia(String evidencia) { this.evidencia = evidencia; }
    public String getEstadoValidacion() { return estadoValidacion; }
    public void setEstadoValidacion(String estadoValidacion) { this.estadoValidacion = estadoValidacion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getJustificacionAnulacion() { return justificacionAnulacion; }
    public void setJustificacionAnulacion(String justificacionAnulacion) { this.justificacionAnulacion = justificacionAnulacion; }
}
