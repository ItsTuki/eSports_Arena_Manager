package com.example.registrationservice.service;

import com.example.registrationservice.dto.InscripcionDtos.EstadoRequest;
import com.example.registrationservice.dto.InscripcionDtos.InscripcionRequest;
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
    public InscripcionService(InscripcionRepository repository) { this.repository = repository; }

    public Inscripcion crear(InscripcionRequest request) {
        boolean abierto = request.torneoAbierto() == null || request.torneoAbierto();
        LocalDateTime cierre = request.fechaCierreInscripcion() == null ? LocalDateTime.now().plusDays(1) : request.fechaCierreInscripcion();
        int cupo = request.cupoMaximo() == null ? Integer.MAX_VALUE : request.cupoMaximo();
        if (!abierto || LocalDateTime.now().isAfter(cierre)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No inscribir fuera de plazo");
        if (Boolean.TRUE.equals(request.participanteSancionado())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No inscribir participante sancionado");
        long ocupados = repository.findAll().stream().filter(i -> Objects.equals(i.getTorneoId(), request.torneoId())).filter(i -> !"CANCELADA".equals(i.getEstado())).count();
        if (ocupados >= cupo) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No superar cupos");

        Inscripcion i = new Inscripcion();
        i.setTorneoId(request.torneoId());
        i.setTipoParticipante(request.tipoParticipante().toUpperCase());
        if ("INDIVIDUAL".equals(i.getTipoParticipante())) {
            if (request.jugadorId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "jugadorId obligatorio");
            validarDuplicado(request.torneoId(), request.jugadorId(), null);
            i.setJugadorId(request.jugadorId());
        } else {
            if (request.equipoId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "equipoId obligatorio");
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
