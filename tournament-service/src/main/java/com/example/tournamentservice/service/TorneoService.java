package com.example.tournamentservice.service;

import com.example.tournamentservice.dto.TorneoDtos.TorneoRequest;
import com.example.tournamentservice.dto.TorneoDtos.TorneoUpdate;
import com.example.tournamentservice.client.GameClient;
import com.example.tournamentservice.model.Torneo;
import com.example.tournamentservice.repository.TorneoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TorneoService {
    private final TorneoRepository repository;
    private final GameClient gameClient;
    public TorneoService(TorneoRepository repository, GameClient gameClient) {
        this.repository = repository;
        this.gameClient = gameClient;
    }

    public Torneo crear(TorneoRequest request) {
        if (!"ACTIVO".equalsIgnoreCase(gameClient.buscar(request.juegoId()).estado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Juego inactivo no permite nuevos torneos");
        }
        validarFechas(request.fechaInicio(), request.fechaFin(), request.fechaCierreInscripcion());
        Torneo t = new Torneo();
        t.setNombre(request.nombre()); t.setJuegoId(request.juegoId()); t.setFechaInicio(request.fechaInicio());
        t.setFechaFin(request.fechaFin()); t.setFechaCierreInscripcion(request.fechaCierreInscripcion());
        t.setCupoMaximo(request.cupoMaximo()); t.setModalidad(request.modalidad().toUpperCase());
        if (request.estado() != null) t.setEstado(request.estado().toUpperCase());
        return repository.save(t);
    }

    public List<Torneo> listar(Long juegoId, String estado, LocalDate fecha) {
        return repository.findAll().stream()
                .filter(t -> juegoId == null || Objects.equals(t.getJuegoId(), juegoId))
                .filter(t -> estado == null || t.getEstado().equalsIgnoreCase(estado))
                .filter(t -> fecha == null || t.getFechaInicio().toLocalDate().equals(fecha))
                .toList();
    }

    public Torneo buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneo no encontrado")); }

    public Torneo actualizar(Long id, TorneoUpdate request) {
        Torneo t = buscar(id);
        if ("EN_CURSO".equals(t.getEstado())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No modificar reglas criticas si el torneo esta en curso");
        LocalDateTime inicio = request.fechaInicio() != null ? request.fechaInicio() : t.getFechaInicio();
        LocalDateTime fin = request.fechaFin() != null ? request.fechaFin() : t.getFechaFin();
        LocalDateTime cierre = request.fechaCierreInscripcion() != null ? request.fechaCierreInscripcion() : t.getFechaCierreInscripcion();
        validarFechas(inicio, fin, cierre);
        t.setFechaInicio(inicio); t.setFechaFin(fin); t.setFechaCierreInscripcion(cierre);
        if (request.cupoMaximo() != null && request.cupoMaximo() > 0) t.setCupoMaximo(request.cupoMaximo());
        if (request.estado() != null) t.setEstado(request.estado().toUpperCase());
        return repository.save(t);
    }

    public Torneo cerrar(Long id) { Torneo t = buscar(id); t.setEstado("CERRADO"); return repository.save(t); }
    public Torneo cancelar(Long id) { Torneo t = buscar(id); t.setEstado("CANCELADO"); return repository.save(t); }

    private void validarFechas(LocalDateTime inicio, LocalDateTime fin, LocalDateTime cierre) {
        if (!inicio.isAfter(cierre)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha de inicio debe ser posterior al cierre de inscripcion");
        if (!fin.isAfter(inicio)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha fin debe ser posterior a fecha inicio");
    }
}
