package com.example.registrationservice.service;

import com.example.registrationservice.dto.InscripcionDtos.EstadoRequest;
import com.example.registrationservice.dto.InscripcionDtos.InscripcionRequest;
import com.example.registrationservice.client.SanctionClient;
import com.example.registrationservice.client.TeamClient;
import com.example.registrationservice.client.TournamentClient;
import com.example.registrationservice.client.UserClient;
import com.example.registrationservice.model.Inscripcion;
import com.example.registrationservice.repository.InscripcionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class InscripcionService {
    private final InscripcionRepository repository;
    private final TournamentClient tournamentClient;
    private final TeamClient teamClient;
    private final UserClient userClient;
    private final SanctionClient sanctionClient;
    public InscripcionService(InscripcionRepository repository, TournamentClient tournamentClient, TeamClient teamClient, UserClient userClient, SanctionClient sanctionClient) {
        this.repository = repository;
        this.tournamentClient = tournamentClient;
        this.teamClient = teamClient;
        this.userClient = userClient;
        this.sanctionClient = sanctionClient;
    }

    public Inscripcion crear(InscripcionRequest request) {
        var torneo = tournamentClient.buscar(request.torneoId());
        boolean abierto = request.torneoAbierto() != null ? request.torneoAbierto() : "ABIERTO".equalsIgnoreCase(torneo.estado());
        LocalDateTime cierre = request.fechaCierreInscripcion() == null ? torneo.fechaCierreInscripcion() : request.fechaCierreInscripcion();
        int cupo = request.cupoMaximo() == null ? torneo.cupoMaximo() : request.cupoMaximo();
        if (!abierto || LocalDateTime.now().isAfter(cierre)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No inscribir fuera de plazo");
        if (Boolean.TRUE.equals(request.participanteSancionado())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No inscribir participante sancionado");
        long ocupados = repository.findAll().stream().filter(i -> Objects.equals(i.getTorneoId(), request.torneoId())).filter(i -> !"CANCELADA".equals(i.getEstado())).count();
        if (ocupados >= cupo) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No superar cupos");

        Inscripcion i = new Inscripcion();
        i.setTorneoId(request.torneoId());
        i.setTipoParticipante(request.tipoParticipante().toUpperCase());
        if ("INDIVIDUAL".equals(i.getTipoParticipante())) {
            if (request.jugadorId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "jugadorId obligatorio");
            if (!Boolean.TRUE.equals(userClient.puedeCompetir(request.jugadorId()).get("puedeCompetir"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario sancionado o inactivo no puede competir");
            if (Boolean.TRUE.equals(sanctionClient.bloqueo(request.jugadorId(), null).get("bloqueaInscripcion"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participante con sancion activa bloqueante");
            validarDuplicado(request.torneoId(), request.jugadorId(), null);
            i.setJugadorId(request.jugadorId());
        } else {
            if (request.equipoId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "equipoId obligatorio");
            if (!Boolean.TRUE.equals(teamClient.puedeInscribirse(request.equipoId()).get("puedeInscribirse"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipo inactivo o sin integrantes suficientes");
            if (Boolean.TRUE.equals(sanctionClient.bloqueo(null, request.equipoId()).get("bloqueaInscripcion"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipo con sancion activa bloqueante");
            validarDuplicado(request.torneoId(), null, request.equipoId());
            i.setEquipoId(request.equipoId());
        }
        return repository.save(i);
    }

    public List<Inscripcion> listar(Long torneoId, Long equipoId, Long jugadorId) {
        return repository.findAll().stream()
                .filter(i -> torneoId == null || Objects.equals(i.getTorneoId(), torneoId))
                .filter(i -> equipoId == null || Objects.equals(i.getEquipoId(), equipoId))
                .filter(i -> jugadorId == null || Objects.equals(i.getJugadorId(), jugadorId))
                .toList();
    }

    public Inscripcion buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscripcion no encontrada")); }
    public Inscripcion actualizarEstado(Long id, EstadoRequest request) { Inscripcion i = buscar(id); i.setEstado(request.estado().toUpperCase()); return repository.save(i); }
    public Inscripcion cancelar(Long id) { Inscripcion i = buscar(id); i.setEstado("CANCELADA"); return repository.save(i); }

    private void validarDuplicado(Long torneoId, Long jugadorId, Long equipoId) {
        boolean existe = repository.findAll().stream().filter(i -> !"CANCELADA".equals(i.getEstado()))
                .anyMatch(i -> Objects.equals(i.getTorneoId(), torneoId) && (jugadorId != null && Objects.equals(i.getJugadorId(), jugadorId) || equipoId != null && Objects.equals(i.getEquipoId(), equipoId)));
        if (existe) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No duplicar inscripcion en el mismo torneo");
    }
}
