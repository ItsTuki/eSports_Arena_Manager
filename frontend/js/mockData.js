/**
 * Datos simulados (Mock Data) para eSports Arena Manager
 * Esquema alineado a los 12 microservicios Spring Boot
 */

export const MOCK_JUEGOS = [
  {
    id: 1,
    nombre: "Valorant Masters",
    categoria: "Tactical Shooter",
    minimoPorEquipo: 5,
    tamanoEquipo: 5,
    descripcion: "Shooter táctico 5v5 con habilidades únicas.",
    activo: true
  },
  {
    id: 2,
    nombre: "League of Legends",
    categoria: "MOBA",
    minimoPorEquipo: 5,
    tamanoEquipo: 5,
    descripcion: "Arena de batalla multijugador 5v5 en la Grieta del Invocador.",
    activo: true
  },
  {
    id: 3,
    nombre: "Rocket League",
    categoria: "Deportes / Autos",
    minimoPorEquipo: 3,
    tamanoEquipo: 3,
    descripcion: "Fútbol acrobático con vehículos propulsados.",
    activo: true
  },
  {
    id: 4,
    nombre: "Street Fighter 6",
    categoria: "Lucha 1v1",
    minimoPorEquipo: 1,
    tamanoEquipo: 1,
    descripcion: "Combates individuales 1v1 al mejor de 3 rounds.",
    activo: true
  }
];

export const MOCK_TORNEOS = [
  {
    id: 1,
    nombre: "Supercopa Valorant 2026",
    juegoId: 1,
    juegoNombre: "Valorant Masters",
    descripcion: "Torneo abierto clasificatorio a la Gran Final Regional.",
    fechaInicio: "2026-09-10T18:00:00",
    fechaFin: "2026-09-15T23:00:00",
    fechaCierreInscripcion: "2026-09-08T23:59:59",
    cupoMaximo: 16,
    cuposOcupados: 8,
    modalidad: "EQUIPO",
    estado: "ABIERTO",
    premioTotal: "$2,500,000 CLP",
    reglas: "Eliminación simple. Partidas al mejor de 3 mapas (BO3)."
  },
  {
    id: 2,
    nombre: "Circuito de Leyendas LoL",
    juegoId: 2,
    juegoNombre: "League of Legends",
    descripcion: "Competencia de alto nivel para escuadras de la región.",
    fechaInicio: "2026-08-20T17:00:00",
    fechaFin: "2026-09-05T22:00:00",
    fechaCierreInscripcion: "2026-08-15T23:59:59",
    cupoMaximo: 8,
    cuposOcupados: 8,
    modalidad: "EQUIPO",
    estado: "EN_CURSO",
    premioTotal: "$3,000,000 CLP",
    reglas: "Fase de grupos y playoffs de doble eliminación."
  },
  {
    id: 3,
    nombre: "Torneo Relámpago Rocket League",
    juegoId: 3,
    juegoNombre: "Rocket League",
    descripcion: "Fin de semana intensivo 3v3 con premios instantáneos.",
    fechaInicio: "2026-08-01T15:00:00",
    fechaFin: "2026-08-03T21:00:00",
    fechaCierreInscripcion: "2026-07-28T23:59:59",
    cupoMaximo: 8,
    cuposOcupados: 8,
    modalidad: "EQUIPO",
    estado: "FINALIZADO",
    premioTotal: "$1,000,000 CLP",
    reglas: "Llave de eliminación directa con repesca."
  },
  {
    id: 4,
    nombre: "Duelo de Maestros SF6",
    juegoId: 4,
    juegoNombre: "Street Fighter 6",
    descripcion: "Torneo individual para determinar al rey de la arena.",
    fechaInicio: "2026-09-25T19:00:00",
    fechaFin: "2026-09-26T23:00:00",
    fechaCierreInscripcion: "2026-09-22T23:59:59",
    cupoMaximo: 32,
    cuposOcupados: 12,
    modalidad: "INDIVIDUAL",
    estado: "ABIERTO",
    premioTotal: "$800,000 CLP",
    reglas: "Bracket de doble eliminación 1v1."
  }
];

export const MOCK_USUARIOS = [
  {
    id: 1,
    nombre: "Admin Principal",
    email: "admin@arena.com",
    rol: "ADMINISTRADOR",
    apodo: "GrandMaster",
    telefono: "+56911223344",
    activo: true
  },
  {
    id: 2,
    nombre: "Organizador General",
    email: "organizador@arena.com",
    rol: "ORGANIZADOR",
    apodo: "RefOfficial",
    telefono: "+56922334455",
    activo: true
  },
  {
    id: 3,
    nombre: "Anibal Romero",
    email: "jugador@arena.com",
    rol: "JUGADOR",
    apodo: "ItsTuki",
    telefono: "+56933445566",
    activo: true,
    victorias: 28,
    derrotas: 9,
    estadisticas: { kda: "3.4", winrate: "75.6%" }
  },
  {
    id: 4,
    nombre: "Victor Guerra",
    email: "victor@arena.com",
    rol: "JUGADOR",
    apodo: "Jazinto",
    telefono: "+56944556677",
    activo: true,
    victorias: 22,
    derrotas: 14,
    estadisticas: { kda: "2.8", winrate: "61.1%" }
  },
  {
    id: 5,
    nombre: "Maximo Lugo",
    email: "maximo@arena.com",
    rol: "JUGADOR",
    apodo: "Tynx",
    telefono: "+56955667788",
    activo: true,
    victorias: 31,
    derrotas: 8,
    estadisticas: { kda: "3.9", winrate: "79.4%" }
  }
];

export const MOCK_EQUIPOS = [
  {
    id: 1,
    nombre: "Nova Esports",
    juegoId: 1,
    juegoNombre: "Valorant Masters",
    capitanId: 3,
    capitanNombre: "ItsTuki",
    estado: "ACTIVO",
    miembros: [
      { usuarioId: 3, apodo: "ItsTuki", rol: "Capitán / Duelista" },
      { usuarioId: 4, apodo: "Jazinto", rol: "Iniciador" },
      { usuarioId: 5, apodo: "Tynx", rol: "Controlador" },
      { usuarioId: 6, apodo: "Shadow", rol: "Centinela" },
      { usuarioId: 7, apodo: "Viper99", rol: "Flex" }
    ]
  },
  {
    id: 2,
    nombre: "Leviathan Gaming",
    juegoId: 2,
    juegoNombre: "League of Legends",
    capitanId: 4,
    capitanNombre: "Jazinto",
    estado: "ACTIVO",
    miembros: [
      { usuarioId: 4, apodo: "Jazinto", rol: "Capitán / Mid" },
      { usuarioId: 8, apodo: "TopKing", rol: "Top" },
      { usuarioId: 9, apodo: "JungleBeast", rol: "Jungla" },
      { usuarioId: 10, apodo: "AdcGod", rol: "ADC" },
      { usuarioId: 11, apodo: "SupportHero", rol: "Support" }
    ]
  },
  {
    id: 3,
    nombre: "Kru Velocity",
    juegoId: 3,
    juegoNombre: "Rocket League",
    capitanId: 5,
    capitanNombre: "Tynx",
    estado: "ACTIVO",
    miembros: [
      { usuarioId: 5, apodo: "Tynx", rol: "Capitán / Delantero" },
      { usuarioId: 12, apodo: "TurboGoal", rol: "Defensa" },
      { usuarioId: 13, apodo: "AerialAce", rol: "Volante" }
    ]
  }
];

export const MOCK_INSCRIPCIONES = [
  {
    id: 1,
    torneoId: 1,
    tipoParticipante: "EQUIPO",
    participanteId: 1,
    participanteNombre: "Nova Esports",
    estado: "APROBADA",
    fechaInscripcion: "2026-08-25T14:20:00"
  },
  {
    id: 2,
    torneoId: 1,
    tipoParticipante: "EQUIPO",
    participanteId: 2,
    participanteNombre: "Leviathan Gaming",
    estado: "APROBADA",
    fechaInscripcion: "2026-08-26T10:15:00"
  },
  {
    id: 3,
    torneoId: 2,
    tipoParticipante: "EQUIPO",
    participanteId: 1,
    participanteNombre: "Nova Esports",
    estado: "APROBADA",
    fechaInscripcion: "2026-08-10T12:00:00"
  },
  {
    id: 4,
    torneoId: 2,
    tipoParticipante: "EQUIPO",
    participanteId: 2,
    participanteNombre: "Leviathan Gaming",
    estado: "APROBADA",
    fechaInscripcion: "2026-08-11T16:45:00"
  },
  {
    id: 5,
    torneoId: 3,
    tipoParticipante: "EQUIPO",
    participanteId: 3,
    participanteNombre: "Kru Velocity",
    estado: "APROBADA",
    fechaInscripcion: "2026-07-25T18:30:00"
  }
];

export const MOCK_PARTIDAS = [
  {
    id: 101,
    torneoId: 2,
    ronda: 1,
    participante1Id: 1,
    participante1Nombre: "Nova Esports",
    participante2Id: 2,
    participante2Nombre: "Leviathan Gaming",
    puntaje1: 2,
    puntaje2: 1,
    ganadorId: 1,
    ganadorNombre: "Nova Esports",
    estado: "FINALIZADA",
    fechaProgramada: "2026-08-22T18:00:00",
    resultadoValidado: true
  },
  {
    id: 102,
    torneoId: 2,
    ronda: 1,
    participante1Id: 3,
    participante1Nombre: "Kru Velocity",
    participante2Id: null,
    participante2Nombre: null,
    puntaje1: 0,
    puntaje2: 0,
    ganadorId: null,
    ganadorNombre: null,
    estado: "PROGRAMADA",
    fechaProgramada: "2026-08-23T20:00:00",
    resultadoValidado: false
  },
  {
    id: 103,
    torneoId: 2,
    ronda: 2,
    participante1Id: 1,
    participante1Nombre: "Nova Esports",
    participante2Id: null,
    participante2Nombre: null,
    puntaje1: 0,
    puntaje2: 0,
    ganadorId: null,
    ganadorNombre: null,
    estado: "PROGRAMADA",
    fechaProgramada: "2026-08-29T19:00:00",
    resultadoValidado: false
  }
];

export const MOCK_RANKINGS = [
  {
    id: 1,
    torneoId: 2,
    participanteId: 1,
    participanteNombre: "Nova Esports",
    partidasJugadas: 3,
    partidasGanadas: 3,
    partidasPerdidas: 0,
    puntos: 9,
    diferenciaPuntaje: 5,
    posicion: 1
  },
  {
    id: 2,
    torneoId: 2,
    participanteId: 2,
    participanteNombre: "Leviathan Gaming",
    partidasJugadas: 3,
    partidasGanadas: 2,
    partidasPerdidas: 1,
    puntos: 6,
    diferenciaPuntaje: 2,
    posicion: 2
  },
  {
    id: 3,
    torneoId: 2,
    participanteId: 3,
    participanteNombre: "Kru Velocity",
    partidasJugadas: 3,
    partidasGanadas: 1,
    partidasPerdidas: 2,
    puntos: 3,
    diferenciaPuntaje: -2,
    posicion: 3
  }
];

export const MOCK_PREMIOS = [
  {
    id: 1,
    torneoId: 3,
    posicion: 1,
    descripcion: "Primer Lugar - Campeón",
    monto: 600000,
    tipo: "EFECTIVO",
    ganadorNombre: "Kru Velocity",
    entregado: true
  },
  {
    id: 2,
    torneoId: 3,
    posicion: 2,
    descripcion: "Segundo Lugar - Subcampeón",
    monto: 300000,
    tipo: "EFECTIVO",
    ganadorNombre: "Nova Esports",
    entregado: true
  },
  {
    id: 3,
    torneoId: 3,
    posicion: 3,
    descripcion: "Tercer Lugar",
    monto: 100000,
    tipo: "EFECTIVO",
    ganadorNombre: "Leviathan Gaming",
    entregado: true
  },
  {
    id: 4,
    torneoId: 1,
    posicion: 1,
    descripcion: "Primer Lugar - Gran Campeón",
    monto: 1500000,
    tipo: "EFECTIVO",
    ganadorNombre: null,
    entregado: false
  },
  {
    id: 5,
    torneoId: 1,
    posicion: 2,
    descripcion: "Segundo Lugar",
    monto: 700000,
    tipo: "EFECTIVO",
    ganadorNombre: null,
    entregado: false
  },
  {
    id: 6,
    torneoId: 1,
    posicion: 3,
    descripcion: "Tercer Lugar",
    monto: 300000,
    tipo: "EFECTIVO",
    ganadorNombre: null,
    entregado: false
  }
];

export const MOCK_SANCIONES = [
  {
    id: 1,
    usuarioId: 6,
    usuarioApodo: "Shadow",
    motivo: "Conducta antideportiva en chat",
    fechaInicio: "2026-08-01T00:00:00",
    fechaFin: "2026-08-05T23:59:59",
    estado: "CUMPLIDA",
    activa: false
  },
  {
    id: 2,
    usuarioId: 14,
    usuarioApodo: "TrollPlayer",
    motivo: "Uso de exploits no autorizados",
    fechaInicio: "2026-08-20T00:00:00",
    fechaFin: "2026-09-20T23:59:59",
    estado: "ACTIVA",
    activa: true
  }
];

export const MOCK_NOTIFICACIONES = [
  {
    id: 1,
    usuarioId: 3,
    titulo: "Inscripción Aprobada",
    mensaje: "Tu equipo Nova Esports ha sido aprobado para la Supercopa Valorant 2026.",
    leida: false,
    fecha: "2026-08-26T10:30:00"
  },
  {
    id: 2,
    usuarioId: 3,
    titulo: "Próxima Partida Programada",
    mensaje: "Tu partida de Ronda 2 ha sido fijada para el 29 de Agosto a las 19:00.",
    leida: true,
    fecha: "2026-08-27T15:00:00"
  }
];

if (typeof window !== 'undefined') {
  window.MockData = {
    MOCK_JUEGOS,
    MOCK_TORNEOS,
    MOCK_USUARIOS,
    MOCK_EQUIPOS,
    MOCK_INSCRIPCIONES,
    MOCK_PARTIDAS,
    MOCK_RANKINGS,
    MOCK_PREMIOS,
    MOCK_SANCIONES,
    MOCK_NOTIFICACIONES
  };
}
