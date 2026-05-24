package com.example.rankingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rankings")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long torneoId;
    @Column(nullable = false)
    private Long participanteId;
    private int puntos;
    private int victorias;
    private int derrotas;
    private int diferencia;
    private int posicion;
    private boolean cerrado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }
    public Long getParticipanteId() { return participanteId; }
    public void setParticipanteId(Long participanteId) { this.participanteId = participanteId; }
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public int getVictorias() { return victorias; }
    public void setVictorias(int victorias) { this.victorias = victorias; }
    public int getDerrotas() { return derrotas; }
    public void setDerrotas(int derrotas) { this.derrotas = derrotas; }
    public int getDiferencia() { return diferencia; }
    public void setDiferencia(int diferencia) { this.diferencia = diferencia; }
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public boolean isCerrado() { return cerrado; }
    public void setCerrado(boolean cerrado) { this.cerrado = cerrado; }
}
