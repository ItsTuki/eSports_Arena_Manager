package com.example.resultservice.service;

import com.example.resultservice.client.MatchClient;
import com.example.resultservice.client.MatchClient.PartidaResponse;
import com.example.resultservice.dto.ResultadoDtos.AnularRequest;
import com.example.resultservice.dto.ResultadoDtos.ResultadoRequest;
import com.example.resultservice.dto.ResultadoDtos.ResultadoUpdate;
import com.example.resultservice.model.Resultado;
import com.example.resultservice.repository.ResultadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {

    @Mock
    private ResultadoRepository repository;

    @Mock
    private MatchClient matchClient;

    @InjectMocks
    private ResultadoService resultadoService;

    private ResultadoRequest requestValido;
    private Resultado resultadoPendiente;
    private Resultado resultadoValidado;

    @BeforeEach
    void setUp() {
        requestValido = new ResultadoRequest(
                1L, // partidaId
                10L, // ganadorId
                3, // puntajeA
                1, // puntajeB
                "http://evidencia.url/screenshot.png",
                true // partidaExiste
        );

        resultadoPendiente = new Resultado();
        resultadoPendiente.setId(1L);
        resultadoPendiente.setPartidaId(1L);
        resultadoPendiente.setGanadorId(10L);
        resultadoPendiente.setPuntajeA(3);
        resultadoPendiente.setPuntajeB(1);
        resultadoPendiente.setEstadoValidacion("PENDIENTE");
        resultadoPendiente.setEvidencia("http://evidencia.url/screenshot.png");

        resultadoValidado = new Resultado();
        resultadoValidado.setId(2L);
        resultadoValidado.setPartidaId(2L);
        resultadoValidado.setGanadorId(20L);
        resultadoValidado.setPuntajeA(2);
        resultadoValidado.setPuntajeB(0);
        resultadoValidado.setEstadoValidacion("VALIDADO");
    }

    @Test
    void crear_ResultadoValido_DebeGuardarYRetornar() {
        // Arrange
        when(matchClient.buscar(1L)).thenReturn(new PartidaResponse(1L, 10L, 11L, "FINALIZADA"));
        when(repository.save(any(Resultado.class))).thenReturn(resultadoPendiente);

        // Act
        Resultado resultado = resultadoService.crear(requestValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.getPuntajeA());
        assertEquals("PENDIENTE", resultado.getEstadoValidacion());
        verify(matchClient, times(1)).buscar(1L);
        verify(repository, times(1)).save(any(Resultado.class));
    }

    @Test
    void crear_PartidaNoExisteSegunRequest_DebeLanzarExcepcion() {
        // Arrange
        ResultadoRequest requestInvalido = new ResultadoRequest(
                99L, 10L, 3, 1, null, false // partidaExiste = false
        );

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            resultadoService.crear(requestInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No registrar resultado de partida inexistente", exception.getReason());
        verify(matchClient, never()).buscar(any());
        verify(repository, never()).save(any());
    }

    @Test
    void listar_PorPartidaId_DebeFiltrarResultados() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(resultadoPendiente, resultadoValidado));

        // Act - Buscamos resultados de la partidaId 2
        List<Resultado> resultados = resultadoService.listar(null, 2L);

        // Assert
        assertEquals(1, resultados.size());
        assertEquals(2L, resultados.get(0).getPartidaId());
        assertEquals("VALIDADO", resultados.get(0).getEstadoValidacion());
    }

    @Test
    void buscar_ResultadoExistente_DebeRetornar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(resultadoPendiente));

        // Act
        Resultado resultado = resultadoService.buscar(1L);

        // Assert
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscar_ResultadoNoExistente_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            resultadoService.buscar(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void actualizar_ResultadoPendienteConPuntajesNegativos_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(resultadoPendiente));
        ResultadoUpdate updateInvalido = new ResultadoUpdate(null, -1, null, null, null, false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            resultadoService.actualizar(1L, updateInvalido);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Puntajes no pueden ser negativos", exception.getReason());
    }

    @Test
    void actualizar_ResultadoValidadoSinRolOrganizador_DebeLanzarExcepcion() {
        // Arrange
        when(repository.findById(2L)).thenReturn(Optional.of(resultadoValidado));

        // Intentamos modificar con rolOrganizador en false
        ResultadoUpdate updateProhibido = new ResultadoUpdate(null, 3, null, null, null, false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            resultadoService.actualizar(2L, updateProhibido);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Resultado validado no puede modificarse sin rol organizador", exception.getReason());
    }

    @Test
    void actualizar_ResultadoValidadoConRolOrganizador_DebeActualizarYGuardar() {
        // Arrange
        when(repository.findById(2L)).thenReturn(Optional.of(resultadoValidado));
        when(repository.save(any(Resultado.class))).thenAnswer(i -> i.getArgument(0));

        // El usuario sí tiene rol organizador en el request
        ResultadoUpdate updateValido = new ResultadoUpdate(null, 4, 1, null, null, true);

        // Act
        Resultado resultado = resultadoService.actualizar(2L, updateValido);

        // Assert
        assertEquals(4, resultado.getPuntajeA());
        assertEquals(1, resultado.getPuntajeB());
        verify(repository, times(1)).save(any(Resultado.class));
    }

    @Test
    void anular_ResultadoExistente_DebeCambiarEstadoYGuardarJustificacion() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(resultadoPendiente));
        when(repository.save(any(Resultado.class))).thenAnswer(i -> i.getArgument(0));

        AnularRequest requestAnular = new AnularRequest("Se detectó uso de software de terceros");

        // Act
        Resultado resultado = resultadoService.anular(1L, requestAnular);

        // Assert
        assertEquals("ANULADO", resultado.getEstadoValidacion());
        assertEquals("Se detectó uso de software de terceros", resultado.getJustificacionAnulacion());
        verify(repository, times(1)).save(resultadoPendiente);
    }
}
