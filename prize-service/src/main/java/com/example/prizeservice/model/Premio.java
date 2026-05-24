package com.example.prizeservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "premios")
public class Premio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long torneoId;
    @Column(nullable = false)
    private int posicion;
    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;
    @Column(nullable = false)
    private String estado = "DISPONIBLE";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTorneoId() { return torneoId; }
    public void setTorneoId(Long torneoId) { this.torneoId = torneoId; }
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
