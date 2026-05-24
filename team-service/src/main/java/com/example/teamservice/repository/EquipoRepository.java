package com.example.teamservice.repository;

import com.example.teamservice.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    List<Equipo> findByJuegoPrincipalId(Long juegoPrincipalId);
    List<Equipo> findByCapitanId(Long capitanId);
    List<Equipo> findByEstadoIgnoreCase(String estado);
}
