package com.example.registrationservice.repository;

import com.example.registrationservice.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByTorneoId(Long torneoId);
    List<Inscripcion> findByEquipoId(Long equipoId);
    List<Inscripcion> findByJugadorId(Long jugadorId);
    boolean existsByTorneoIdAndJugadorIdAndEstadoNot(Long torneoId, Long jugadorId, String estado);
    boolean existsByTorneoIdAndEquipoIdAndEstadoNot(Long torneoId, Long equipoId, String estado);
    long countByTorneoIdAndEstadoNot(Long torneoId, String estado);
}
