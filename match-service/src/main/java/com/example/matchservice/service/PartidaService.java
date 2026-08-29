package com.example.matchservice.service;

import com.example.matchservice.dto.PartidaDtos.PartidaRequest;
import com.example.matchservice.dto.PartidaDtos.PartidaUpdate;
import com.example.matchservice.client.RegistrationClient;
import com.example.matchservice.client.TournamentClient;
import com.example.matchservice.model.Partida;
import com.example.matchservice.repository.PartidaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;

@Service
public class PartidaService {
    private final PartidaRepository repository;
    private final TournamentClient tournamentClient;
    private final RegistrationClient registrationClient;
    public PartidaService(PartidaRepository repository, TournamentClient tournamentClient, RegistrationClient registrationClient) {
        this.repository = repository;
        this.tournamentClient = tournamentClient;
        this.registrationClient = registrationClient;
    }

    public Partida crear(PartidaRequest request) {
        tournamentClient.buscar(request.torneoId());
        validarParticipantes(request.participanteAId(), request.participanteBId(), request.participanteAInscrito(), request.participanteBInscrito());
        validarInscrito(request.torneoId(), request.participanteAId());
        validarInscrito(request.torneoId(), request.participanteBId());
        if (duplicada(request.torneoId(), request.ronda(), request.participanteAId(), request.participanteBId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No duplicar enfrentamiento en la misma ronda");
        Partida p = new Partida();
        p.setTorneoId(request.torneoId()); p.setParticipanteAId(request.participanteAId()); p.setParticipanteBId(request.participanteBId());
        p.setRonda(request.ronda()); p.setFechaHora(request.fechaHora());
        return repository.save(p);
    }

    public List<Partida> listar(Long torneoId, Integer ronda, String estado) {
        return repository.findAll().stream()
                .filter(p -> torneoId == null || Objects.equals(p.getTorneoId(), torneoId))
                .filter(p -> ronda == null || Objects.equals(p.getRonda(), ronda))
                .filter(p -> estado == null || p.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public Partida buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida no encontrada")); }

    public Partida actualizar(Long id, PartidaUpdate request) {
        Partida p = buscar(id);
        if ("CANCELADA".equals(p.getEstado()) && "EN_CURSO".equalsIgnoreCase(String.valueOf(request.estado()))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No iniciar partida cancelada");
        if (request.participanteAId() != null) p.setParticipanteAId(request.participanteAId());
        if (request.participanteBId() != null) p.setParticipanteBId(request.participanteBId());
        validarParticipantes(p.getParticipanteAId(), p.getParticipanteBId(), true, true);
        if (request.fechaHora() != null) p.setFechaHora(request.fechaHora());
        if (request.estado() != null) p.setEstado(request.estado().toUpperCase());
        return repository.save(p);
    }

    public Partida cancelar(Long id) { Partida p = buscar(id); p.setEstado("CANCELADA"); return repository.save(p); }

    private void validarParticipantes(Long a, Long b, Boolean aInscrito, Boolean bInscrito) {
        if (Objects.equals(a, b)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participantes deben ser distintos");
        if (Boolean.FALSE.equals(aInscrito) || Boolean.FALSE.equals(bInscrito)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No crear partida con participante no inscrito");
    }
    private boolean duplicada(Long torneoId, Integer ronda, Long a, Long b) {
        return repository.findAll().stream().anyMatch(p -> Objects.equals(p.getTorneoId(), torneoId) && Objects.equals(p.getRonda(), ronda)
                && ((Objects.equals(p.getParticipanteAId(), a) && Objects.equals(p.getParticipanteBId(), b)) || (Objects.equals(p.getParticipanteAId(), b) && Objects.equals(p.getParticipanteBId(), a))));
    }

    private void validarInscrito(Long torneoId, Long participanteId) {
        boolean inscrito = registrationClient.listar(torneoId).stream()
                .filter(i -> "ACEPTADA".equalsIgnoreCase(i.estado()))
                .anyMatch(i -> Objects.equals(i.jugadorId(), participanteId) || Objects.equals(i.equipoId(), participanteId));
        if (!inscrito) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No crear partida con participante no inscrito");
    }
}
