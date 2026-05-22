package com.registrationservice.repository;

import com.registrationservice.model.Inscripcion;
import com.registrationservice.model.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {


    boolean existsByTorneoIdAndJugadorIdAndEstadoNot(Long torneoId, Long jugadorId, EstadoInscripcion estado);
    boolean existsByTorneoIdAndEquipoIdAndEstadoNot(Long torneoId, Long equipoId, EstadoInscripcion estado);

    long countByTorneoIdAndEstadoIn(Long torneoId, List<EstadoInscripcion> estados);

    @Query("SELECT i FROM Inscripcion i WHERE " +
            "(:torneoId IS NULL OR i.torneoId = :torneoId) AND " +
            "(:equipoId IS NULL OR i.equipoId = :equipoId) AND " +
            "(:jugadorId IS NULL OR i.jugadorId = :jugadorId)")
    List<Inscripcion> buscarInscripcionesFiltradas(
            @Param("torneoId") Long torneoId,
            @Param("equipoId") Long equipoId,
            @Param("jugadorId") Long jugadorId);
}