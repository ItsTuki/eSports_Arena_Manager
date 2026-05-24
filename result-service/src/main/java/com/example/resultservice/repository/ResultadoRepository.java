package com.example.resultservice.repository;

import com.example.resultservice.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    List<Resultado> findByPartidaId(Long partidaId);
    List<Resultado> findByEstadoValidacionIgnoreCase(String estadoValidacion);
}
