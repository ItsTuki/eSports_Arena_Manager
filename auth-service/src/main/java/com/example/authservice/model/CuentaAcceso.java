package com.example.authservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CuentaAcceso {
    private Long id;
    private String email;
    private String passwordHash;
    private String rol;
    private String estado = "ACTIVO";
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private List<String> historial = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public List<String> getHistorial() { return historial; }
    public void setHistorial(List<String> historial) { this.historial = historial; }
}
