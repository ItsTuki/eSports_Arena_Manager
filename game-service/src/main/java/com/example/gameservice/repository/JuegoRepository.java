package com.example.gameservice.repository;

import com.example.gameservice.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JuegoRepository extends JpaRepository<Juego, Long> {
    Optional<Juego> findByNombre(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Juego> findByEstadoIgnoreCase(String estado);
}
