package com.gameservice.repository;

import com.gameservice.model.Juego;
import com.gameservice.model.EstadoJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {
    List<Juego> findByEstado(EstadoJuego estado);
    boolean existsByNombreIgnoreCase(String nombre);
}