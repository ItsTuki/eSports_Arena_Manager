package com.example.tournamentservice.repository;

import com.example.tournamentservice.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByJuegoId(Long juegoId);
    List<Torneo> findByEstadoIgnoreCase(String estado);
}
