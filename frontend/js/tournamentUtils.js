/**
 * Utilidades de lógica de negocio para eSports Arena Manager
 * Implementación de funciones core especificadas en EP2 y utilizadas en EP1/EP3.
 */

// 1. ordenarRanking: Ordena por puntos (descendente) y desempata por diferencia de puntaje (descendente)
export function ordenarRanking(rankingList = []) {
  return [...rankingList].sort((a, b) => {
    const puntosA = Number(a.puntos || 0);
    const puntosB = Number(b.puntos || 0);
    if (puntosB !== puntosA) {
      return puntosB - puntosA;
    }
    const diffA = Number(a.diferenciaPuntaje !== undefined ? a.diferenciaPuntaje : (a.puntosFavor || 0) - (a.puntosContra || 0));
    const diffB = Number(b.diferenciaPuntaje !== undefined ? b.diferenciaPuntaje : (b.puntosFavor || 0) - (b.puntosContra || 0));
    return diffB - diffA;
  });
}

// 2. calcularPuntos: Asigna puntos por victoria y por derrota a partir de una lista de resultados
export function calcularPuntos(resultados = [], puntosPorVictoria = 3, puntosPorDerrota = 0, puntosPorEmpate = 1) {
  let puntos = 0;
  for (const res of resultados) {
    if (res.victoria === true || res.resultado === 'VICTORIA' || res.ganador === true) {
      puntos += puntosPorVictoria;
    } else if (res.empate === true || res.resultado === 'EMPATE') {
      puntos += puntosPorEmpate;
    } else if (res.derrota === true || res.resultado === 'DERROTA' || res.ganador === false) {
      puntos += puntosPorDerrota;
    }
  }
  return puntos;
}

// 3. cuposDisponibles: Resta inscripciones vigentes al cupo máximo y nunca retorna un valor negativo
export function cuposDisponibles(cupoMaximo, inscripcionesVigentes) {
  const max = Math.max(0, Number(cupoMaximo) || 0);
  const inscritos = Math.max(0, Number(inscripcionesVigentes) || 0);
  const disponibles = max - inscritos;
  return disponibles < 0 ? 0 : disponibles;
}

// 4. inscripcionFueraDePlazo: Retorna true cuando la fecha actual supera el cierre de inscripción
export function inscripcionFueraDePlazo(fechaCierre, fechaActual = new Date()) {
  if (!fechaCierre) return false;
  const cierre = new Date(fechaCierre);
  const actual = new Date(fechaActual);
  return actual.getTime() > cierre.getTime();
}

// 5. tieneSancionActiva: Bloquea al participante con sanción vigente y permite al que ya la cumplió
export function tieneSancionActiva(sanciones = [], fechaActual = new Date()) {
  if (!sanciones || !Array.isArray(sanciones) || sanciones.length === 0) {
    return false;
  }
  const actualTime = new Date(fechaActual).getTime();
  return sanciones.some(sancion => {
    // Si tiene flag explícito de activa o fecha de fin posterior a la actual
    if (sancion.activa === false || sancion.estado === 'CUMPLIDA' || sancion.estado === 'INACTIVA') {
      return false;
    }
    if (sancion.activa === true || sancion.estado === 'ACTIVA') {
      if (sancion.fechaFin) {
        return new Date(sancion.fechaFin).getTime() >= actualTime;
      }
      return true;
    }
    if (sancion.fechaFin) {
      return new Date(sancion.fechaFin).getTime() >= actualTime;
    }
    return false;
  });
}

// 6. equipoCompleto: Valida la cantidad de integrantes exigida por el juego del torneo
export function equipoCompleto(equipo, juego) {
  if (!equipo) return false;
  const cantidadIntegrantes = Array.isArray(equipo.miembros) 
    ? equipo.miembros.length 
    : (Number(equipo.cantidadMiembros) || (Array.isArray(equipo.jugadores) ? equipo.jugadores.length : 0));
  
  const requeridos = Number(juego?.minimoPorEquipo || juego?.tamanoEquipo || juego?.integrantesPorEquipo || 1);
  return cantidadIntegrantes >= requeridos;
}

// Exponer en objeto global para compatibilidad UMD y Jasmine Karma
if (typeof window !== 'undefined') {
  window.TournamentUtils = {
    ordenarRanking,
    calcularPuntos,
    cuposDisponibles,
    inscripcionFueraDePlazo,
    tieneSancionActiva,
    equipoCompleto
  };
}
