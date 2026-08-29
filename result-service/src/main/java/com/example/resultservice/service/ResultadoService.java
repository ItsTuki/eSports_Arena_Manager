package com.example.resultservice.service;

import com.example.resultservice.dto.ResultadoDtos.AnularRequest;
import com.example.resultservice.dto.ResultadoDtos.ResultadoRequest;
import com.example.resultservice.dto.ResultadoDtos.ResultadoUpdate;
import com.example.resultservice.client.MatchClient;
import com.example.resultservice.model.Resultado;
import com.example.resultservice.repository.ResultadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;

@Service
public class ResultadoService {
    private final ResultadoRepository repository;
    private final MatchClient matchClient;
    public ResultadoService(ResultadoRepository repository, MatchClient matchClient) {
        this.repository = repository;
        this.matchClient = matchClient;
    }

    public Resultado crear(ResultadoRequest request) {
        if (Boolean.FALSE.equals(request.partidaExiste())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No registrar resultado de partida inexistente");
        matchClient.buscar(request.partidaId());
        Resultado r = new Resultado();
        r.setPartidaId(request.partidaId()); r.setGanadorId(request.ganadorId()); r.setPuntajeA(request.puntajeA()); r.setPuntajeB(request.puntajeB()); r.setEvidencia(request.evidencia());
        return repository.save(r);
    }
    public List<Resultado> listar(Long torneoId, Long partidaId) { return repository.findAll().stream().filter(r -> partidaId == null || Objects.equals(r.getPartidaId(), partidaId)).toList(); }
    public Resultado buscar(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resultado no encontrado")); }
    public Resultado actualizar(Long id, ResultadoUpdate request) {
        Resultado r = buscar(id);
        if ("VALIDADO".equals(r.getEstadoValidacion()) && !Boolean.TRUE.equals(request.rolOrganizador())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Resultado validado no puede modificarse sin rol organizador");
        if (request.puntajeA() != null && request.puntajeA() < 0 || request.puntajeB() != null && request.puntajeB() < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Puntajes no pueden ser negativos");
        if (request.ganadorId() != null) r.setGanadorId(request.ganadorId());
        if (request.puntajeA() != null) r.setPuntajeA(request.puntajeA());
        if (request.puntajeB() != null) r.setPuntajeB(request.puntajeB());
        if (request.evidencia() != null) r.setEvidencia(request.evidencia());
        if (request.estadoValidacion() != null) r.setEstadoValidacion(request.estadoValidacion().toUpperCase());
        return repository.save(r);
    }
    public Resultado anular(Long id, AnularRequest request) { Resultado r = buscar(id); r.setEstadoValidacion("ANULADO"); r.setJustificacionAnulacion(request.justificacion()); return repository.save(r); }
}
