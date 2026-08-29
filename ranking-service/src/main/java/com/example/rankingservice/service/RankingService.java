package com.example.rankingservice.service;

import com.example.rankingservice.dto.RankingDtos.RankingRequest;
import com.example.rankingservice.dto.RankingDtos.RankingUpdate;
import com.example.rankingservice.model.Ranking;
import com.example.rankingservice.repository.RankingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class RankingService {
    private final RankingRepository repository;
    public RankingService(RankingRepository repository) { this.repository = repository; }
    public Ranking crear(RankingRequest request) {
        if (repository.findAll().stream().anyMatch(r -> Objects.equals(r.getTorneoId(), request.torneoId()) && Objects.equals(r.getParticipanteId(), request.participanteId()))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No duplicar participante en ranking");
        Ranking r = new Ranking(); r.setTorneoId(request.torneoId()); r.setParticipanteId(request.participanteId()); repository.save(r); recalcular(request.torneoId()); return r;
    }
    public List<Ranking> listar(Long torneoId) { return repository.findAll().stream().filter(r -> torneoId == null || Objects.equals(r.getTorneoId(), torneoId)).sorted(Comparator.comparingInt(Ranking::getPosicion)).toList(); }
    public Ranking posicion(Long torneoId, Long participanteId) { return repository.findAll().stream().filter(r -> Objects.equals(r.getTorneoId(), torneoId) && Objects.equals(r.getParticipanteId(), participanteId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Posicion no encontrada")); }
    public Ranking actualizar(Long id, RankingUpdate request) {
        if (!Boolean.TRUE.equals(request.resultadoValidado())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo resultados validados actualizan ranking");
        Ranking r = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ranking no encontrado"));
        if (r.isCerrado()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ranking cerrado");
        if (request.puntos() != null) r.setPuntos(request.puntos()); if (request.victorias() != null) r.setVictorias(request.victorias()); if (request.derrotas() != null) r.setDerrotas(request.derrotas()); if (request.diferencia() != null) r.setDiferencia(request.diferencia());
        repository.save(r); recalcular(r.getTorneoId()); return r;
    }
    public List<Ranking> cerrar(Long torneoId) { listar(torneoId).forEach(r -> { r.setCerrado(true); repository.save(r); }); return listar(torneoId); }
    public List<Ranking> reiniciar(Long torneoId) { listar(torneoId).forEach(r -> { r.setPuntos(0); r.setVictorias(0); r.setDerrotas(0); r.setDiferencia(0); r.setCerrado(false); repository.save(r); }); recalcular(torneoId); return listar(torneoId); }
    private void recalcular(Long torneoId) {
        List<Ranking> ordenados = repository.findAll().stream().filter(r -> Objects.equals(r.getTorneoId(), torneoId)).sorted(Comparator.comparingInt(Ranking::getPuntos).reversed().thenComparing(Comparator.comparingInt(Ranking::getDiferencia).reversed())).toList();
        for (int i = 0; i < ordenados.size(); i++) { ordenados.get(i).setPosicion(i + 1); repository.save(ordenados.get(i)); }
    }
}
