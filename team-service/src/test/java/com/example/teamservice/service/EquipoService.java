package com.teamservice.service;

import com.teamservice.model.*;
import com.teamservice.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EquipoService {

    private final EquipoRepository repository;

    public EquipoService(EquipoRepository repository) {
        this.repository = repository;
    }

    // 1. Crear Equipo
    @Transactional
    public Equipo crearEquipo(Equipo equipo) {
        validarReglasIntegrantes(equipo.getCapitanId(), equipo.getIntegrantes());

        // Mapear los integrantes adjuntos en el JSON para que apunten correctamente a la entidad Padre
        List<MiembroEquipo> copiaIntegrantes = new ArrayList<>(equipo.getIntegrantes());
        equipo.limpiarIntegrantes();

        for (MiembroEquipo m : copiaIntegrantes) {
            equipo.agregarIntegrante(m.getUsuarioId(), m.getRolDentroEquipo());
        }

        return repository.save(equipo);
    }

    // 2. Listar equipos por juego, capitán o estado
    public List<Equipo> listarEquipos(Long juegoId, Long capitanId, EstadoEquipo estado) {
        return repository.buscarEquiposFiltrados(juegoId, capitanId, estado);
    }

    // 3. Buscar equipo por ID
    public Optional<Equipo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // 4. Actualizar nombre, capitán o integrantes
    @Transactional
    public Equipo actualizarEquipo(Long id, String nuevoNombre, Long nuevoCapitanId, List<MiembroEquipo> nuevosIntegrantes) {
        Equipo equipo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado con el ID: " + id));

        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            equipo.setNombre(nuevoNombre);
        }

        // Si se alteran los integrantes o el capitán, se deben re-validar las reglas de negocio
        Long capitanFinal = (nuevoCapitanId != null) ? nuevoCapitanId : equipo.getCapitanId();
        List<MiembroEquipo> integrantesFinales = (nuevosIntegrantes != null) ? nuevosIntegrantes : equipo.getIntegrantes();

        if (nuevoCapitanId != null || nuevosIntegrantes != null) {
            validarReglasIntegrantes(capitanFinal, integrantesFinales);
            equipo.setCapitanId(capitanFinal);

            if (nuevosIntegrantes != null) {
                equipo.limpiarIntegrantes();
                for (MiembroEquipo m : nuevosIntegrantes) {
                    equipo.agregarIntegrante(m.getUsuarioId(), m.getRolDentroEquipo());
                }
            }
        }

        return repository.save(equipo);
    }

    // 5. Desactivar Equipo (Cambio de estado sin borrado físico)
    @Transactional
    public void desactivarEquipo(Long id) {
        Equipo equipo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado con el ID: " + id));
        equipo.setEstado(EstadoEquipo.INACTIVE);
        repository.save(equipo);
    }

    // --- VALIDACIONES DE REGLAS DE NEGOCIO ---
    private void validarReglasIntegrantes(Long capitanId, List<MiembroEquipo> integrantes) {
        if (integrantes == null || integrantes.isEmpty()) {
            throw new IllegalArgumentException("El equipo debe contener al menos un integrante (el capitán).");
        }

        Set<Long> usuarioIdsUnicos = new HashSet<>();
        boolean capitanPresenteEnLista = false;

        for (MiembroEquipo miembro : integrantes) {
            // Regla: No duplicar jugador dentro del mismo equipo
            if (!usuarioIdsUnicos.add(miembro.getUsuarioId())) {
                throw new IllegalArgumentException("El jugador con ID " + miembro.getUsuarioId() + " está duplicado en el equipo.");
            }

            // Validar consistencia del capitán dentro de la lista de miembros
            if (miembro.getUsuarioId().equals(capitanId)) {
                if (miembro.getRolDentroEquipo() != RolMiembro.CAPITAN) {
                    throw new IllegalArgumentException("El usuario definido como capitanId debe tener el rol de CAPITAN en la lista de integrantes.");
                }
                capitanPresenteEnLista = true;
            } else if (miembro.getRolDentroEquipo() == RolMiembro.CAPITAN) {
                throw new IllegalArgumentException("No puede haber más de un CAPITAN en el equipo.");
            }
        }

        // Regla: Un equipo debe tener capitán
        if (!capitanPresenteEnLista) {
            throw new IllegalArgumentException("El capitán con ID " + capitanId + " debe ser parte de la lista de integrantes del equipo.");
        }
    }
}