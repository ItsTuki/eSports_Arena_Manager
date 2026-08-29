package com.example.sanctionservice.repository;

import com.example.sanctionservice.model.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SancionRepository extends JpaRepository<Sancion, Long> {
    List<Sancion> findByUsuarioId(Long usuarioId);
    List<Sancion> findByEquipoId(Long equipoId);
    List<Sancion> findByEstadoIgnoreCase(String estado);
}
