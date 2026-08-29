package com.example.matchservice.repository;

import com.example.matchservice.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByTorneoId(Long torneoId);
    List<Partida> findByRonda(Integer ronda);
    List<Partida> findByEstadoIgnoreCase(String estado);
}
