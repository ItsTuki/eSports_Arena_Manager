/**
 * Suite de Pruebas Unitarias - Jasmine (EP2)
 * Casos 1 al 6: Lógica de negocio y reglas del dominio
 */

describe("Pruebas Unitarias de Reglas de Negocio - eSports Arena Manager", function() {

  // Test 1: ordenarRanking
  it("1. ordenarRanking ordena por puntos y desempata por diferencia de puntaje", function() {
    const rankingDesordenado = [
      { participanteNombre: "Equipo B", puntos: 6, diferenciaPuntaje: 2 },
      { participanteNombre: "Equipo A", puntos: 9, diferenciaPuntaje: 5 },
      { participanteNombre: "Equipo C", puntos: 6, diferenciaPuntaje: 4 }, // Mismos puntos que B pero mejor diferencia
      { participanteNombre: "Equipo D", puntos: 3, diferenciaPuntaje: -1 }
    ];

    const resultado = window.TournamentUtils.ordenarRanking(rankingDesordenado);

    expect(resultado.length).toBe(4);
    expect(resultado[0].participanteNombre).toBe("Equipo A"); // 9 pts
    expect(resultado[1].participanteNombre).toBe("Equipo C"); // 6 pts, diff +4
    expect(resultado[2].participanteNombre).toBe("Equipo B"); // 6 pts, diff +2
    expect(resultado[3].participanteNombre).toBe("Equipo D"); // 3 pts
  });

  // Test 2: calcularPuntos
  it("2. calcularPuntos asigna los puntos definidos por victoria y por derrota a partir de una lista de resultados", function() {
    const resultados = [
      { victoria: true },
      { victoria: true },
      { victoria: false, derrota: true },
      { victoria: true }
    ];
    // 3 victorias (+3 cada una = 9) y 1 derrota (+0 = 0) => total 9 puntos
    const puntosCalculados = window.TournamentUtils.calcularPuntos(resultados, 3, 0, 1);
    expect(puntosCalculados).toBe(9);

    const resultadosConEmpate = [
      { victoria: true },
      { empate: true },
      { derrota: true }
    ];
    // 1 victoria (3) + 1 empate (1) + 1 derrota (0) => 4 puntos
    expect(window.TournamentUtils.calcularPuntos(resultadosConEmpate, 3, 0, 1)).toBe(4);
  });

  // Test 3: cuposDisponibles
  it("3. cuposDisponibles resta las inscripciones vigentes al cupo máximo y nunca retorna un valor negativo", function() {
    // Caso normal
    expect(window.TournamentUtils.cuposDisponibles(16, 5)).toBe(11);
    // Caso límite: lleno
    expect(window.TournamentUtils.cuposDisponibles(8, 8)).toBe(0);
    // Caso sobrecupo o error de entrada: nunca negativo
    expect(window.TournamentUtils.cuposDisponibles(8, 12)).toBe(0);
    expect(window.TournamentUtils.cuposDisponibles(0, 5)).toBe(0);
  });

  // Test 4: inscripcionFueraDePlazo
  it("4. inscripcionFueraDePlazo retorna verdadero cuando la fecha actual supera el cierre de inscripción", function() {
    const fechaCierrePasada = "2026-08-01T23:59:59";
    const fechaActualPosterior = new Date("2026-08-10T12:00:00");
    const fechaActualPrevia = new Date("2026-07-20T12:00:00");

    // Fuera de plazo
    expect(window.TournamentUtils.inscripcionFueraDePlazo(fechaCierrePasada, fechaActualPosterior)).toBe(true);
    // A tiempo
    expect(window.TournamentUtils.inscripcionFueraDePlazo(fechaCierrePasada, fechaActualPrevia)).toBe(false);
  });

  // Test 5: tieneSancionActiva
  it("5. tieneSancionActiva bloquea al participante con sanción vigente y permite al que ya la cumplió", function() {
    const fechaReferencia = new Date("2026-08-28T12:00:00");

    const sancionesConSancionVigente = [
      { motivo: "Trampa", fechaInicio: "2026-08-20T00:00:00", fechaFin: "2026-09-20T23:59:59", activa: true, estado: "ACTIVA" }
    ];

    const sancionesCumplidas = [
      { motivo: "Spam", fechaInicio: "2026-07-01T00:00:00", fechaFin: "2026-07-10T23:59:59", activa: false, estado: "CUMPLIDA" }
    ];

    const sinSanciones = [];

    expect(window.TournamentUtils.tieneSancionActiva(sancionesConSancionVigente, fechaReferencia)).toBe(true);
    expect(window.TournamentUtils.tieneSancionActiva(sancionesCumplidas, fechaReferencia)).toBe(false);
    expect(window.TournamentUtils.tieneSancionActiva(sinSanciones, fechaReferencia)).toBe(false);
  });

  // Test 6: equipoCompleto
  it("6. equipoCompleto valida la cantidad de integrantes exigida por el juego del torneo", function() {
    const juegoValorant5v5 = { id: 1, nombre: "Valorant", minimoPorEquipo: 5 };
    const equipoDe4 = { id: 1, nombre: "Equipo Incompleto", miembros: [1, 2, 3, 4] };
    const equipoDe5 = { id: 2, nombre: "Equipo Completo", miembros: [1, 2, 3, 4, 5] };
    const equipoDe6 = { id: 3, nombre: "Equipo con Suplente", miembros: [1, 2, 3, 4, 5, 6] };

    expect(window.TournamentUtils.equipoCompleto(equipoDe4, juegoValorant5v5)).toBe(false);
    expect(window.TournamentUtils.equipoCompleto(equipoDe5, juegoValorant5v5)).toBe(true);
    expect(window.TournamentUtils.equipoCompleto(equipoDe6, juegoValorant5v5)).toBe(true);
  });
});
