package com.example.teamservice.service;

import com.example.teamservice.dto.EquipoDtos.EquipoRequest;
import com.example.teamservice.dto.EquipoDtos.EquipoUpdate;
import com.example.teamservice.dto.EquipoDtos.MiembroRequest;
import com.example.teamservice.client.GameClient;
import com.example.teamservice.client.UserClient;
import com.example.teamservice.model.Equipo;
import com.example.teamservice.model.MiembroEquipo;
import com.example.teamservice.repository.EquipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class EquipoService {
    private final EquipoRepository repository;
    private final UserClient userClient;
    private final GameClient gameClient;

    public EquipoService(EquipoRepository repository, UserClient userClient, GameClient gameClient) {
        this.repository = repository;
        this.userClient = userClient;
        this.gameClient = gameClient;
    }

    public Equipo crear(EquipoRequest request) {
        validarUsuarioPuedeCompetir(request.capitanId());
        validarJuegoActivo(request.juegoPrincipalId());
        Equipo equipo = new Equipo();
        equipo.setNombre(request.nombre());
        equipo.setCapitanId(request.capitanId());
        equipo.setJuegoPrincipalId(request.juegoPrincipalId());
        equipo.getIntegrantes().addAll(construirIntegrantes(equipo, request.integrantes()));
        return repository.save(equipo);
    }

    public List<Equipo> listar(Long juegoId, Long capitanId, String estado) {
        return repository.findAll().stream()
                .filter(e -> juegoId == null || Objects.equals(e.getJuegoPrincipalId(), juegoId))
                .filter(e -> capitanId == null || Objects.equals(e.getCapitanId(), capitanId))
                .filter(e -> estado == null || e.getEstado().equalsIgnoreCase(estado))
                .toList();
    }

    public Equipo buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado"));
    }

    public Equipo actualizar(Long id, EquipoUpdate request) {
        Equipo equipo = buscar(id);
        if (request.nombre() != null) equipo.setNombre(request.nombre());
        if (request.capitanId() != null) equipo.setCapitanId(request.capitanId());
        if (request.integrantes() != null) {
            equipo.getIntegrantes().clear();
            equipo.getIntegrantes().addAll(construirIntegrantes(equipo, request.integrantes()));
        }
        if (request.estado() != null) equipo.setEstado(request.estado().toUpperCase());
        return repository.save(equipo);
    }

    public Equipo agregarMiembro(Long id, MiembroRequest request) {
        Equipo equipo = buscar(id);
        boolean duplicado = equipo.getIntegrantes().stream().anyMatch(m -> Objects.equals(m.getUsuarioId(), request.usuarioId()));
        if (duplicado) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jugador duplicado dentro del equipo");
        equipo.getIntegrantes().add(miembro(equipo, request.usuarioId(), request.rolDentroEquipo()));
        return repository.save(equipo);
    }

    public Equipo desactivar(Long id) {
        Equipo equipo = buscar(id);
        equipo.setEstado("INACTIVO");
        return repository.save(equipo);
    }

    public boolean puedeInscribirse(Long id) {
        Equipo equipo = buscar(id);
        return "ACTIVO".equals(equipo.getEstado()) && !equipo.getIntegrantes().isEmpty();
    }

    private List<MiembroEquipo> construirIntegrantes(Equipo equipo, List<MiembroRequest> requests) {
        Map<Long, MiembroEquipo> miembros = new LinkedHashMap<>();
        if (requests != null) {
            for (MiembroRequest request : requests) {
                if (miembros.containsKey(request.usuarioId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Jugador duplicado dentro del equipo");
                miembros.put(request.usuarioId(), miembro(equipo, request.usuarioId(), request.rolDentroEquipo()));
            }
        }
        miembros.putIfAbsent(equipo.getCapitanId(), miembro(equipo, equipo.getCapitanId(), "CAPITAN"));
        return new ArrayList<>(miembros.values());
    }

    private MiembroEquipo miembro(Equipo equipo, Long usuarioId, String rol) {
        validarUsuarioPuedeCompetir(usuarioId);
        MiembroEquipo miembro = new MiembroEquipo();
        miembro.setEquipo(equipo);
        miembro.setUsuarioId(usuarioId);
        miembro.setRolDentroEquipo(rol);
        return miembro;
    }

    private void validarUsuarioPuedeCompetir(Long usuarioId) {
        Boolean puede = userClient.puedeCompetir(usuarioId).get("puedeCompetir");
        if (!Boolean.TRUE.equals(puede)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario inactivo o sancionado no puede integrar equipo");
    }

    private void validarJuegoActivo(Long juegoId) {
        if (!"ACTIVO".equalsIgnoreCase(gameClient.buscar(juegoId).estado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Juego inactivo no permite equipos nuevos");
        }
    }
}
