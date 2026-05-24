package com.example.prizeservice.service;

import com.example.prizeservice.dto.PremioDtos.AsignarRequest;
import com.example.prizeservice.dto.PremioDtos.PremioRequest;
import com.example.prizeservice.dto.PremioDtos.PremioUpdate;
import com.example.prizeservice.client.RankingClient;
import com.example.prizeservice.client.TournamentClient;
import com.example.prizeservice.model.Premio;
import com.example.prizeservice.model.PremioAsignado;
import com.example.prizeservice.repository.PremioAsignadoRepository;
import com.example.prizeservice.repository.PremioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;

@Service
public class PremioService {
    private final PremioRepository repository;
    private final PremioAsignadoRepository asignadoRepository;
    private final TournamentClient tournamentClient;
    private final RankingClient rankingClient;
    public PremioService(PremioRepository repository, PremioAsignadoRepository asignadoRepository, TournamentClient tournamentClient, RankingClient rankingClient) {
        this.repository = repository;
        this.asignadoRepository = asignadoRepository;
        this.tournamentClient = tournamentClient;
        this.rankingClient = rankingClient;
    }
    public Premio crear(PremioRequest request) {
        if (repository.findAll().stream().anyMatch(p -> Objects.equals(p.getTorneoId(), request.torneoId()) && p.getPosicion() == request.posicion())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No duplicar premio para la misma posicion");
        Premio p = new Premio(); p.setTorneoId(request.torneoId()); p.setPosicion(request.posicion()); p.setDescripcion(request.descripcion()); if (request.valor() != null) p.setValor(request.valor()); return repository.save(p);
    }
    public List<Premio> listar(Long torneoId, Integer posicion) { return repository.findAll().stream().filter(p -> torneoId == null || Objects.equals(p.getTorneoId(), torneoId)).filter(p -> posicion == null || p.getPosicion() == posicion).toList(); }
    public Premio buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Premio no encontrado")); }
    public Premio actualizar(Long id, PremioUpdate request) {
        Premio p = buscar(id); if ("ASIGNADO".equals(p.getEstado()) && !Boolean.TRUE.equals(request.autorizado())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No modificar premio asignado sin autorizacion");
        if (request.descripcion() != null) p.setDescripcion(request.descripcion()); if (request.valor() != null) p.setValor(request.valor()); if (request.estado() != null) p.setEstado(request.estado().toUpperCase()); return repository.save(p);
    }
    public Premio desactivar(Long id) { Premio p = buscar(id); if ("ASIGNADO".equals(p.getEstado())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Premio ya asignado"); p.setEstado("INACTIVO"); return repository.save(p); }
    public PremioAsignado asignar(Long id, AsignarRequest request) {
        Premio p = buscar(id);
        boolean torneoFinalizado = request.torneoFinalizado() != null ? request.torneoFinalizado() : "FINALIZADO".equalsIgnoreCase(tournamentClient.buscar(p.getTorneoId()).estado());
        boolean rankingValidado = request.rankingValidado() != null ? request.rankingValidado() : rankingClient.listar(p.getTorneoId()).stream().anyMatch(r -> r.posicion() == p.getPosicion() && r.cerrado());
        if (!torneoFinalizado || !rankingValidado) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No asignar premios antes de finalizar torneo y validar ranking");
        p.setEstado("ASIGNADO"); repository.save(p); PremioAsignado a = new PremioAsignado(); a.setPremioId(id); a.setParticipanteId(request.participanteId()); return asignadoRepository.save(a);
    }
}
