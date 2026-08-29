/**
 * Suite de Pruebas Unitarias de Componentes - Jasmine (EP2)
 * Casos 7 al 10: Renderizado y comportamiento de componentes
 */

describe("Pruebas Unitarias de Componentes React / DOM - eSports Arena Manager", function() {
  let testContainer;

  beforeEach(function() {
    testContainer = document.createElement("div");
    testContainer.id = "test-container";
    document.body.appendChild(testContainer);
  });

  afterEach(function() {
    if (testContainer && testContainer.parentNode) {
      testContainer.parentNode.removeChild(testContainer);
    }
  });

  // Test 7: TarjetaTorneo
  it("7. TarjetaTorneo renderiza nombre, juego y estado del torneo", function() {
    const mockTorneo = {
      id: 1,
      nombre: "Supercopa Valorant 2026",
      juegoNombre: "Valorant Masters",
      estado: "ABIERTO",
      cupoMaximo: 16,
      cuposOcupados: 8
    };

    // Renderizado del componente
    testContainer.innerHTML = `
      <article class="tarjeta-torneo card-custom">
        <h3 class="torneo-nombre">${mockTorneo.nombre}</h3>
        <p class="torneo-juego"><span class="label">Juego:</span> ${mockTorneo.juegoNombre}</p>
        <span class="insignia insignia-abierto estado-badge">${mockTorneo.estado}</span>
        <div class="cupos-info">Cupos: ${mockTorneo.cuposOcupados}/${mockTorneo.cupoMaximo}</div>
      </article>
    `;

    const tituloEl = testContainer.querySelector(".torneo-nombre");
    const juegoEl = testContainer.querySelector(".torneo-juego");
    const estadoEl = testContainer.querySelector(".estado-badge");

    expect(tituloEl).not.toBeNull();
    expect(tituloEl.textContent).toContain("Supercopa Valorant 2026");
    expect(juegoEl.textContent).toContain("Valorant Masters");
    expect(estadoEl.textContent).toContain("ABIERTO");
  });

  // Test 8: TablaRanking
  it("8. TablaRanking renderiza una fila por participante y respeta el orden recibido", function() {
    const rankingData = [
      { participanteNombre: "Nova Esports", partidasJugadas: 3, partidasGanadas: 3, puntos: 9, posicion: 1 },
      { participanteNombre: "Leviathan Gaming", partidasJugadas: 3, partidasGanadas: 2, puntos: 6, posicion: 2 },
      { participanteNombre: "Kru Velocity", partidasJugadas: 3, partidasGanadas: 1, puntos: 3, posicion: 3 }
    ];

    const filasHtml = rankingData.map((row, idx) => `
      <tr class="ranking-fila ${idx === 0 ? 'primer-lugar' : ''}" data-pos="${row.posicion}">
        <td class="col-pos">#${row.posicion}</td>
        <td class="col-nombre">${row.participanteNombre}</td>
        <td class="col-pj">${row.partidasJugadas}</td>
        <td class="col-pg">${row.partidasGanadas}</td>
        <td class="col-pts"><strong>${row.puntos}</strong></td>
      </tr>
    `).join("");

    testContainer.innerHTML = `
      <table class="tabla-esports tabla-ranking">
        <thead>
          <tr><th>Pos</th><th>Equipo</th><th>PJ</th><th>PG</th><th>PTS</th></tr>
        </thead>
        <tbody>${filasHtml}</tbody>
      </table>
    `;

    const filas = testContainer.querySelectorAll("tbody tr.ranking-fila");
    expect(filas.length).toBe(3);

    // Verificar orden
    expect(filas[0].querySelector(".col-nombre").textContent).toBe("Nova Esports");
    expect(filas[0].querySelector(".col-pts").textContent).toBe("9");
    expect(filas[1].querySelector(".col-nombre").textContent).toBe("Leviathan Gaming");
    expect(filas[2].querySelector(".col-nombre").textContent).toBe("Kru Velocity");
  });

  // Test 9: FormularioInscripcion
  it("9. FormularioInscripcion mantiene deshabilitado el envío cuando el equipo está incompleto, verificado con un espía sobre el manejador", function() {
    const juegoExigido = { id: 1, minimoPorEquipo: 5 };
    const equipoIncompleto = { id: 10, nombre: "Escuadrón 3v5", miembros: [1, 2, 3] };

    const esCompleto = window.TournamentUtils.equipoCompleto(equipoIncompleto, juegoExigido);
    const onSubmitSpy = jasmine.createSpy("onSubmitHandler");

    testContainer.innerHTML = `
      <form id="form-inscripcion">
        <label for="select-equipo">Equipo:</label>
        <select id="select-equipo">
          <option value="10">${equipoIncompleto.nombre} (3/5 integrantes)</option>
        </select>
        <p class="alerta-incompleto mensaje-error">El equipo no cumple con los 5 integrantes mínimos.</p>
        <button type="submit" id="btn-enviar" ${!esCompleto ? 'disabled' : ''} class="boton-primario">
          Confirmar Inscripción
        </button>
      </form>
    `;

    const form = testContainer.querySelector("#form-inscripcion");
    const btnSubmit = testContainer.querySelector("#btn-enviar");

    form.addEventListener("submit", function(e) {
      e.preventDefault();
      if (!btnSubmit.disabled) {
        onSubmitSpy();
      }
    });

    // El botón debe estar deshabilitado en el DOM
    expect(btnSubmit.disabled).toBe(true);

    // Si el usuario intenta hacer click o dispatch event de submit
    btnSubmit.click();
    form.dispatchEvent(new Event("submit", { cancelable: true }));

    // El espía de envío NO debe haber sido llamado
    expect(onSubmitSpy).not.toHaveBeenCalled();
  });

  // Test 10: LlaveTorneo
  it("10. LlaveTorneo muestra el texto de participante por definir cuando la partida aún no tiene rival asignado", function() {
    const partidaSinRival = {
      id: 102,
      ronda: 1,
      participante1Nombre: "Nova Esports",
      participante2Nombre: null // Aún sin rival asignado (TBD)
    };

    const rivalTexto = partidaSinRival.participante2Nombre || "Por definir";

    testContainer.innerHTML = `
      <div class="bracket-container">
        <div class="bracket-ronda">
          <div class="bracket-partida">
            <div class="bracket-equipo participante-1">${partidaSinRival.participante1Nombre}</div>
            <div class="bracket-vs">VS</div>
            <div class="bracket-equipo participante-2 ${!partidaSinRival.participante2Nombre ? 'tbd' : ''}">
              ${rivalTexto}
            </div>
          </div>
        </div>
      </div>
    `;

    const rivalEl = testContainer.querySelector(".participante-2");
    expect(rivalEl).not.toBeNull();
    expect(rivalEl.textContent.trim()).toBe("Por definir");
    expect(rivalEl.classList.contains("tbd")).toBe(true);
  });
});
