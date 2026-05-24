package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificacionDtos.NotificacionRequest;
import com.example.notificationservice.dto.NotificacionDtos.NotificacionUpdate;
import com.example.notificationservice.model.Notificacion;
import com.example.notificationservice.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {
    private final NotificacionService service;
    public NotificacionController(NotificacionService service) { this.service = service; }
    @PostMapping public ResponseEntity<Notificacion> crear(@Valid @RequestBody NotificacionRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
    @GetMapping public List<Notificacion> listar(@RequestParam(required = false) Long usuarioId, @RequestParam(required = false) Long equipoId) { return service.listar(usuarioId, equipoId); }
    @GetMapping("/{id}") public Notificacion buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Notificacion actualizar(@PathVariable Long id, @RequestBody NotificacionUpdate request) { return service.actualizar(id, request); }
    @PatchMapping("/{id}/leer") public Notificacion leer(@PathVariable Long id) { return service.leer(id); }
    @PatchMapping("/{id}/archivar") public Notificacion archivar(@PathVariable Long id) { return service.archivar(id); }
}
