package com.example.rankingservice.repository;

import com.example.rankingservice.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findByTorneoId(Long torneoId);
    boolean existsByTorneoIdAndParticipanteId(Long torneoId, Long participanteId);
}
