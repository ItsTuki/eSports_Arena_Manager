package com.example.sanctionservice.service;

import com.example.sanctionservice.dto.SancionDtos.SancionRequest;
import com.example.sanctionservice.dto.SancionDtos.SancionUpdate;
import com.example.sanctionservice.client.TeamClient;
import com.example.sanctionservice.client.UserClient;
import com.example.sanctionservice.model.Sancion;
import com.example.sanctionservice.repository.SancionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SancionService {
    private final SancionRepository repository;
    private final UserClient userClient;
    private final TeamClient teamClient;
    public SancionService(SancionRepository repository, UserClient userClient, TeamClient teamClient) {
        this.repository = repository;
        this.userClient = userClient;
        this.teamClient = teamClient;
    }
    public Sancion crear(SancionRequest request) {
        if (request.usuarioId() == null && request.equipoId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sancion requiere usuario o equipo");
        if (request.usuarioId() != null) userClient.buscar(request.usuarioId());
        if (request.equipoId() != null) teamClient.buscar(request.equipoId());
        validarFechas(request.fechaInicio(), request.fechaFin());
        Sancion s = new Sancion(); s.setUsuarioId(request.usuarioId()); s.setEquipoId(request.equipoId()); s.setMotivo(request.motivo()); s.setFechaInicio(request.fechaInicio()); s.setFechaFin(request.fechaFin()); s.setSeveridad(request.severidad().toUpperCase());
        return repository.save(s);
    }
    public List<Sancion> listar(Long usuarioId, Long equipoId, String estado) { return repository.findAll().stream().filter(s -> usuarioId == null || Objects.equals(s.getUsuarioId(), usuarioId)).filter(s -> equipoId == null || Objects.equals(s.getEquipoId(), equipoId)).filter(s -> estado == null || s.getEstado().equalsIgnoreCase(estado)).toList(); }
    public Sancion buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sancion no encontrada")); }
    public Sancion actualizar(Long id, SancionUpdate request) {
        Sancion s = buscar(id); LocalDate inicio = request.fechaInicio() != null ? request.fechaInicio() : s.getFechaInicio(); LocalDate fin = request.fechaFin() != null ? request.fechaFin() : s.getFechaFin(); validarFechas(inicio, fin);
        s.setFechaInicio(inicio); s.setFechaFin(fin); if (request.motivo() != null) s.setMotivo(request.motivo()); if (request.estado() != null) s.setEstado(request.estado().toUpperCase()); if (request.severidad() != null) s.setSeveridad(request.severidad().toUpperCase()); return repository.save(s);
    }
    public Sancion cerrar(Long id) { Sancion s = buscar(id); s.setEstado("CERRADA"); return repository.save(s); }
    public boolean bloqueaInscripcion(Long usuarioId, Long equipoId) { return repository.findAll().stream().anyMatch(s -> "ACTIVA".equals(s.getEstado()) && "BLOQUEANTE".equals(s.getSeveridad()) && !LocalDate.now().isAfter(s.getFechaFin()) && (usuarioId != null && Objects.equals(s.getUsuarioId(), usuarioId) || equipoId != null && Objects.equals(s.getEquipoId(), equipoId))); }
    private void validarFechas(LocalDate inicio, LocalDate fin) { if (!fin.isAfter(inicio)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha fin debe ser posterior a fecha inicio"); }
}
