package com.example.prizeservice.repository;

import com.example.prizeservice.model.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PremioRepository extends JpaRepository<Premio, Long> {
    List<Premio> findByTorneoId(Long torneoId);
    List<Premio> findByPosicion(Integer posicion);
    boolean existsByTorneoIdAndPosicion(Long torneoId, int posicion);
}
