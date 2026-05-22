package com.teamservice.repository;

import com.teamservice.model.Equipo;
import com.teamservice.model.EstadoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    @Query("SELECT e FROM Equipo e WHERE " +
            "(:juegoId IS NULL OR e.juegoPrincipalId = :juegoId) AND " +
            "(:capitanId IS NULL OR e.capitanId = :capitanId) AND " +
            "(:estado IS NULL OR e.estado = :estado)")
    List<Equipo> buscarEquiposFiltrados(
            @Param("juegoId") Long juegoId,
            @Param("capitanId") Long capitanId,
            @Param("estado") EstadoEquipo estado);
}