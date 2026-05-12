package com.eSports.arena.registration.repository;

import com.eSports.arena.registration.model.Inscripcion;
import com.eSports.arena.registration.model.Inscripcion.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Inscripcionrepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByTorneoId(Long torneoId);

    List<Inscripcion> findByEquipoId(Long equipoId);

    List<Inscripcion> findByJugadorId(Long jugadorId);

    List<Inscripcion> findByEstado(EstadoInscripcion estado);

    /** Verifica duplicado: mismo torneo + equipo (o jugador) */
    Optional<Inscripcion> findByTorneoIdAndEquipoId(Long torneoId, Long equipoId);

    Optional<Inscripcion> findByTorneoIdAndJugadorId(Long torneoId, Long jugadorId);

    /** Cuenta inscripciones confirmadas de un torneo (para validar cupo) */
    long countByTorneoIdAndEstado(Long torneoId, EstadoInscripcion estado);
}
