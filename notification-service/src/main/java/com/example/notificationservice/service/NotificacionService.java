package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificacionDtos.NotificacionRequest;
import com.example.notificationservice.dto.NotificacionDtos.NotificacionUpdate;
import com.example.notificationservice.model.Notificacion;
import com.example.notificationservice.repository.NotificacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;

@Service
public class NotificacionService {
    private final NotificacionRepository repository;
    public NotificacionService(NotificacionRepository repository) { this.repository = repository; }
    public Notificacion crear(NotificacionRequest request) {
        if (request.usuarioId() == null && request.equipoId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No crear notificacion sin destinatario");
        Notificacion n = new Notificacion(); n.setUsuarioId(request.usuarioId()); n.setEquipoId(request.equipoId()); n.setTipo(request.tipo().toUpperCase()); n.setMensaje(request.mensaje()); return repository.save(n);
    }
    public List<Notificacion> listar(Long usuarioId, Long equipoId) { return repository.findAll().stream().filter(n -> usuarioId == null || Objects.equals(n.getUsuarioId(), usuarioId)).filter(n -> equipoId == null || Objects.equals(n.getEquipoId(), equipoId)).toList(); }
    public Notificacion buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificacion no encontrada")); }
    public Notificacion actualizar(Long id, NotificacionUpdate request) { Notificacion n = buscar(id); if (request.leida() != null) { n.setLeida(request.leida()); if (request.leida()) n.setEstado("LEIDA"); } if (request.estado() != null) n.setEstado(request.estado().toUpperCase()); return repository.save(n); }
    public Notificacion leer(Long id) { Notificacion n = buscar(id); n.setLeida(true); n.setEstado("LEIDA"); return repository.save(n); }
    public Notificacion archivar(Long id) { Notificacion n = buscar(id); n.setEstado("ARCHIVADA"); return repository.save(n); }
}
