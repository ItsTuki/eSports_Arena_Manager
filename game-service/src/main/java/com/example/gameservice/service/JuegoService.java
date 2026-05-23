package com.gameservice.service;

import com.gameservice.dto.JuegoRequestDTO;
import com.gameservice.model.Juego;
import com.gameservice.model.EstadoJuego;
import com.gameservice.model.Modalidad;
import com.gameservice.repository.JuegoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoService {

    private static final Logger log = LoggerFactory.getLogger(JuegoService.class);
    private final JuegoRepository repository;

    public JuegoService(JuegoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Juego crearJuego(JuegoRequestDTO dto) {
        log.info("Intentando registrar un nuevo juego con nombre: {}", dto.getNombre());

        if (repository.existsByNombreIgnoreCase(dto.getNombre())) {
            log.error("Validación fallida: El juego '{}' ya se encuentra registrado.", dto.getNombre());
            throw new IllegalArgumentException("El nombre del videojuego ya está registrado.");
        }

        Juego juego = new Juego();
        juego.setNombre(dto.getNombre());
        juego.setGenero(dto.getGenero());
        juego.setModalidad(dto.getModalidad());
        juego.setJugadoresPorEquipo(dto.getJugadoresPorEquipo());
        juego.setReglasGenerales(dto.getReglasGenerales());

        Juego guardado = repository.save(juego);
        log.info("Juego registrado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    // 2. Listar juegos activos
    public List<Juego> listarJuegosActivos() {
        log.info("Consultando lista de videojuegos activos en el sistema.");
        return repository.findByEstado(EstadoJuego.ACTIVO);
    }

    // 3. Buscar juego por ID
    public Optional<Juego> buscarPorId(Long id) {
        log.info("Buscando videojuego por ID: {}", id);
        return repository.findById(id);
    }


    @Transactional
    public Juego actualizarJuego(Long id, Modalidad nuevaModalidad, String nuevasReglas) {
        log.info("Procesando actualización para el juego con ID: {}", id);

        Juego juego = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El videojuego con el ID provisto no existe."));

        if (nuevaModalidad != null) {
            log.info("Cambiando modalidad de {} a {}", juego.getModalidad(), nuevaModalidad);
            juego.setModalidad(nuevaModalidad);
        }
        if (nuevasReglas != null) {
            log.info("Actualizando las reglas generales del juego.");
            juego.setReglasGenerales(nuevasReglas);
        }

        return repository.save(juego);
    }


    @Transactional
    public void desactivarJuego(Long id) {
        log.warn("Solicitud de desactivación para el juego con ID: {}", id);

        Juego juego = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("El videojuego con el ID provisto no existe."));

        juego.setEstado(EstadoJuego.INACTIVO);
        repository.save(juego);
        log.info("El juego '{}' ha sido marcado como INACTIVO de forma correcta.", juego.getNombre());
    }
}