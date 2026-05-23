package com.registrationservice.service;

import com.registrationservice.client.ClientModels.*;
import com.registrationservice.dto.InscripcionRequestDTO;
import com.registrationservice.model.*;
import com.registrationservice.repository.InscripcionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionService.class);
    private final InscripcionRepository repository;

    public InscripcionService(InscripcionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Inscripcion crearInscripcion(InscripcionRequestDTO dto) {
        log.info("Iniciando flujo integrador de inscripción para el Torneo ID: {}", dto.getTorneoId());


        TournamentMock torneo = new TournamentMock(dto.getTorneoId(), "ABIERTO", 16, LocalDateTime.now().plusDays(2));
        SanctionMock sancion = new SanctionMock(1L, false); // Falsa simulación: no está sancionado


        if (!"ABIERTO".equalsIgnoreCase(torneo.estado()) || LocalDateTime.now().isAfter(torneo.fechaCierreInscripcion())) {
            log.error("Validación Fallida: Torneo fuera de plazo de inscripción o cerrado.");
            throw new IllegalStateException("El período de inscripción para este torneo ha finalizado o no está abierto.");
        }


        long cuposOcupados = repository.countByTorneoIdAndEstadoIn(dto.getTorneoId(), List.of(EstadoInscripcion.PROCESANDO, EstadoInscripcion.ACEPTADA));
        if (cuposOcupados >= torneo.cupoMaximo()) {
            log.error("Validación Fallida: Cupos agotados para el Torneo ID: {}", dto.getTorneoId());
            throw new IllegalArgumentException("No quedan cupos disponibles para este torneo.");
        }

        if (sancion.activaBloqueante()) {
            log.error("Validación Fallida: El participante posee una sanción activa bloqueante.");
            throw new SecurityException("Operación denegada: El participante se encuentra sancionado.");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setTorneoId(dto.getTorneoId());
        inscripcion.setTipoParticipante(dto.getTipoParticipante());

        // REGLA 4: No duplicar inscripción en el mismo torneo
        if (dto.getTipoParticipante() == TipoParticipante.INDIVIDUAL) {
            if (dto.getJugadorId() == null) throw new IllegalArgumentException("El campo jugadorId es obligatorio para la modalidad INDIVIDUAL.");

            if (repository.existsByTorneoIdAndJugadorIdAndEstadoNot(dto.getTorneoId(), dto.getJugadorId(), EstadoInscripcion.RECHAZADA)) {
                throw new IllegalArgumentException("El jugador ya cuenta con una inscripción activa para este torneo.");
            }
            inscripcion.setJugadorId(dto.getJugadorId());
        } else {
            if (dto.getEquipoId() == null) throw new IllegalArgumentException("El campo equipoId es obligatorio para la modalidad por EQUIPOS.");

            if (repository.existsByTorneoIdAndEquipoIdAndEstadoNot(dto.getTorneoId(), dto.getEquipoId(), EstadoInscripcion.RECHAZADA)) {
                throw new IllegalArgumentException("El equipo ya se encuentra inscrito en este torneo.");
            }
            inscripcion.setEquipoId(dto.getEquipoId());
        }

        inscripcion.setEstado(EstadoInscripcion.ACEPTADA);
        log.info("Inscripción procesada y aceptada exitosamente.");
        return repository.save(inscripcion);
    }

    public List<Inscripcion> listarInscripciones(Long torneoId, Long equipoId, Long jugadorId) {
        log.info("Buscando inscripciones bajo criterios específicos.");
        return repository.buscarInscripcionesFiltradas(torneoId, equipoId, jugadorId);
    }


    public Optional<Inscripcion> buscarPorId(Long id) {
        log.info("Consultando inscripción con ID: {}", id);
        return repository.findById(id);
    }


    @Transactional
    public Inscripcion actualizarEstado(Long id, EstadoInscripcion nuevoEstado) {
        log.info("Cambiando estado de inscripción ID: {} a {}", id, nuevoEstado);
        Inscripcion inscripcion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada."));

        inscripcion.setEstado(nuevoEstado);
        return repository.save(inscripcion);
    }

    @Transactional
    public void cancelarInscripcion(Long id) {
        log.warn("Solicitud de cancelación para la inscripción ID: {}", id);
        Inscripcion inscripcion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada."));

        inscripcion.setEstado(EstadoInscripcion.CANCELADA);
        repository.save(inscripcion);
    }
}