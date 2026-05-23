package com.example.gameservice.service;

import com.example.gameservice.dto.JuegoDtos.JuegoRequest;
import com.example.gameservice.dto.JuegoDtos.JuegoUpdate;
import com.example.gameservice.model.Juego;
import com.example.gameservice.repository.JuegoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JuegoService {
    private final JuegoRepository repository;

    public JuegoService(JuegoRepository repository) { this.repository = repository; }

    public Juego crear(JuegoRequest request) {
        repository.findByNombre(request.nombre()).ifPresent(j -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de juego ya registrado"); });
        Juego juego = new Juego();
        juego.setNombre(request.nombre());
        juego.setGenero(request.genero());
        juego.setModalidad(request.modalidad().toUpperCase());
        juego.setJugadoresPorEquipo(request.jugadoresPorEquipo());
        juego.setReglasGenerales(request.reglasGenerales());
        return repository.save(juego);
    }

    public List<Juego> activos() {
        return repository.findAll().stream().filter(j -> "ACTIVO".equals(j.getEstado())).toList();
    }

    public Juego buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Juego no encontrado"));
    }

    public Juego actualizar(Long id, JuegoUpdate request) {
        Juego juego = buscar(id);
        if (request.modalidad() != null) juego.setModalidad(request.modalidad().toUpperCase());
        if (request.reglasGenerales() != null) juego.setReglasGenerales(request.reglasGenerales());
        if (request.estado() != null) juego.setEstado(request.estado().toUpperCase());
        return repository.save(juego);
    }

    public Juego desactivar(Long id) {
        Juego juego = buscar(id);
        juego.setEstado("INACTIVO");
        return repository.save(juego);
    }
}
