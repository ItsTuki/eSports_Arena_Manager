/**
 * eSports Arena Manager - Base de Datos Simulada (EP1)
 * Datos en memoria para juegos, torneos, participantes, equipos, partidas, rankings, sanciones y premios.
 */

const DB = {
    // 1. Juegos Habilitados
    juegos: [
        {
            id: 1,
            nombre: "League of Legends",
            slug: "lol",
            genero: "MOBA",
            minIntegrantes: 5,
            modalidad: "5v5",
            imagen: "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&w=600&q=80",
            descripcion: "Juego de estrategia por equipos en el que dos equipos de cinco campeones se enfrentan para destruir la base rival."
        },
        {
            id: 2,
            nombre: "Valorant",
            slug: "valorant",
            genero: "Shooter Tactico",
            minIntegrantes: 5,
            modalidad: "5v5",
            imagen: "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?auto=format&fit=crop&w=600&q=80",
            descripcion: "Shooter tactico en primera persona donde la precision de tiro se combina con habilidades de personajes unicos."
        },
        {
            id: 3,
            nombre: "Rocket League",
            slug: "rocket-league",
            genero: "Deportivo / Conduccion",
            minIntegrantes: 3,
            modalidad: "3v3",
            imagen: "https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=600&q=80",
            descripcion: "Futbol arcade de alta potencia y propulsion con vehiculos acrobaticos en estadios cerrados."
        },
        {
            id: 4,
            nombre: "Street Fighter 6",
            slug: "sf6",
            genero: "Lucha (Fighting)",
            minIntegrantes: 1,
            modalidad: "1v1",
            imagen: "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=600&q=80",
            descripcion: "Combate competitivo individual mano a mano con el sistema Drive y roster legendario."
        }
    ],

    // 2. Usuarios y Jugadores Registrados
    usuarios: [
        {
            id: 1,
            apodo: "ShadowStriker",
            nombreCompleto: "Matias Silva",
            email: "matias.silva@arenaesports.cl",
            rol: "JUGADOR",
            avatar: "https://images.unsplash.com/photo-1566492031773-4f4e44671857?auto=format&fit=crop&w=200&q=80",
            victorias: 28,
            derrotas: 12,
            rango: "Diamante I",
            activo: true,
            sanciones: []
        },
        {
            id: 2,
            apodo: "Valkyria99",
            nombreCompleto: "Camila Rojas",
            email: "camila.rojas@arenaesports.cl",
            rol: "JUGADOR",
            avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80",
            victorias: 34,
            derrotas: 8,
            rango: "Master",
            activo: true,
            sanciones: []
        },
        {
            id: 3,
            apodo: "ToxicRage",
            nombreCompleto: "Ignacio Soto",
            email: "ignacio.soto@arenaesports.cl",
            rol: "JUGADOR",
            avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80",
            victorias: 10,
            derrotas: 15,
            rango: "Oro II",
            activo: true,
            sanciones: [
                {
                    id: 101,
                    motivo: "Conducta antideportiva y lenguaje ofensivo en chat general",
                    fechaInicio: "2026-08-20",
                    fechaFin: "2026-09-30",
                    estado: "VIGENTE"
                }
            ]
        },
        {
            id: 4,
            apodo: "CyberKnight",
            nombreCompleto: "Lucas Morales",
            email: "lucas.morales@arenaesports.cl",
            rol: "JUGADOR",
            avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=200&q=80",
            victorias: 22,
            derrotas: 14,
            rango: "Platino II",
            activo: true,
            sanciones: [
                {
                    id: 102,
                    motivo: "Desconexion deliberada en semifinales",
                    fechaInicio: "2026-01-10",
                    fechaFin: "2026-02-10",
                    estado: "CUMPLIDA"
                }
            ]
        },
        {
            id: 5,
            apodo: "PixelQueen",
            nombreCompleto: "Valentina Henriquez",
            email: "valentina.h@arenaesports.cl",
            rol: "JUGADOR",
            avatar: "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=200&q=80",
            victorias: 19,
            derrotas: 9,
            rango: "Diamante III",
            activo: true,
            sanciones: []
        },
        {
            id: 6,
            apodo: "NexusAdmin",
            nombreCompleto: "Anibal Romero",
            email: "admin@arenaesports.cl",
            rol: "ADMINISTRADOR",
            avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=200&q=80",
            victorias: 0,
            derrotas: 0,
            rango: "Administrador de Plataforma",
            activo: true,
            sanciones: []
        },
        {
            id: 7,
            apodo: "RefereePro",
            nombreCompleto: "Victor Guerra",
            email: "organizador@arenaesports.cl",
            rol: "ORGANIZADOR",
            avatar: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=200&q=80",
            victorias: 0,
            derrotas: 0,
            rango: "Juez Principal de Arena",
            activo: true,
            sanciones: []
        }
    ],

    // 3. Equipos
    equipos: [
        {
            id: 1,
            nombre: "Apex Legends Vanguard",
            juegoId: 1,
            capitanId: 1,
            activo: true,
            fechaCreacion: "2026-02-15",
            integrantes: [
                { usuarioId: 1, apodo: "ShadowStriker", rol: "Capitan / Mid" },
                { usuarioId: 2, apodo: "Valkyria99", rol: "ADC" },
                { usuarioId: 4, apodo: "CyberKnight", rol: "Top" },
                { usuarioId: 5, apodo: "PixelQueen", rol: "Support" },
                { usuarioId: 3, apodo: "ToxicRage", rol: "Jungler" }
            ]
        },
        {
            id: 2,
            nombre: "Quantum Phantoms",
            juegoId: 2,
            capitanId: 2,
            activo: true,
            fechaCreacion: "2026-03-01",
            integrantes: [
                { usuarioId: 2, apodo: "Valkyria99", rol: "Capitana / Duelista" },
                { usuarioId: 1, apodo: "ShadowStriker", rol: "Iniciador" },
                { usuarioId: 4, apodo: "CyberKnight", rol: "Controlador" },
                { usuarioId: 5, apodo: "PixelQueen", rol: "Centinela" }
            ]
        },
        {
            id: 3,
            nombre: "Turbo Boosters",
            juegoId: 3,
            capitanId: 4,
            activo: true,
            fechaCreacion: "2026-04-10",
            integrantes: [
                { usuarioId: 4, apodo: "CyberKnight", rol: "Capitan / Delantero" },
                { usuarioId: 1, apodo: "ShadowStriker", rol: "Defensa" },
                { usuarioId: 5, apodo: "PixelQueen", rol: "Volante" }
            ]
        }
    ],

    // 4. Torneos
    torneos: [
        {
            id: 1,
            nombre: "Copa Invocadores Pro Apertura 2026",
            juegoId: 1,
            juegoNombre: "League of Legends",
            modalidad: "5v5 Equipos",
            tipoParticipante: "EQUIPO",
            estado: "ABIERTO",
            fechaInicio: "2026-09-20",
            fechaFin: "2026-10-05",
            cierreInscripcion: "2026-09-15",
            cupoMaximo: 8,
            cuposOcupados: 5,
            organizador: "RefereePro",
            banner: "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&w=1200&q=80",
            descripcion: "Torneo oficial de apertura para escuadras de League of Legends en formato eliminatoria directa al mejor de 3 partidas (Bo3).",
            requisitos: [
                "Equipo con minimo 5 jugadores inscritos",
                "Jugadores sin sanciones disciplinarias vigentes",
                "Nivel minimo de cuenta 30 en servidor LAS",
                "Inscripcion antes del 15 de Septiembre de 2026"
            ],
            premios: [
                { posicion: 1, recompensa: "$500.000 CLP + Trofeo + 5000 RP" },
                { posicion: 2, recompensa: "$250.000 CLP + Medalla de Plata + 2500 RP" },
                { posicion: 3, recompensa: "$100.000 CLP + Medalla de Bronce + 1500 RP" }
            ],
            inscritos: [
                { id: 101, participanteId: 1, nombre: "Apex Legends Vanguard", tipo: "EQUIPO", fecha: "2026-08-15" },
                { id: 102, participanteId: 8, nombre: "Dragon Slayers", tipo: "EQUIPO", fecha: "2026-08-18" },
                { id: 103, participanteId: 9, nombre: "Cyber Wolves", tipo: "EQUIPO", fecha: "2026-08-22" },
                { id: 104, participanteId: 10, nombre: "Southern Frost", tipo: "EQUIPO", fecha: "2026-08-25" },
                { id: 105, participanteId: 11, nombre: "Neon Blitz", tipo: "EQUIPO", fecha: "2026-08-30" }
            ],
            partidas: [
                {
                    ronda: "Cuartos de Final",
                    id: 1,
                    equipoA: "Apex Legends Vanguard",
                    equipoB: "Dragon Slayers",
                    fechaHora: "2026-09-20 18:00",
                    estado: "PROGRAMADA",
                    resultado: null
                },
                {
                    ronda: "Cuartos de Final",
                    id: 2,
                    equipoA: "Cyber Wolves",
                    equipoB: "Southern Frost",
                    fechaHora: "2026-09-20 20:30",
                    estado: "PROGRAMADA",
                    resultado: null
                },
                {
                    ronda: "Semifinales",
                    id: 3,
                    equipoA: "Por definir (Ganador P1)",
                    equipoB: "Por definir (Ganador P2)",
                    fechaHora: "2026-09-27 19:00",
                    estado: "PROGRAMADA",
                    resultado: null
                },
                {
                    ronda: "Gran Final",
                    id: 4,
                    equipoA: "Por definir",
                    equipoB: "Por definir",
                    fechaHora: "2026-10-05 21:00",
                    estado: "PROGRAMADA",
                    resultado: null
                }
            ],
            ranking: [
                { posicion: 1, participante: "Apex Legends Vanguard", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 },
                { posicion: 2, participante: "Dragon Slayers", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 },
                { posicion: 3, participante: "Cyber Wolves", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 },
                { posicion: 4, participante: "Southern Frost", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 },
                { posicion: 5, participante: "Neon Blitz", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 }
            ]
        },
        {
            id: 2,
            nombre: "Valorant Spike Master Series 2026",
            juegoId: 2,
            juegoNombre: "Valorant",
            modalidad: "5v5 Equipos",
            tipoParticipante: "EQUIPO",
            estado: "EN_CURSO",
            fechaInicio: "2026-08-10",
            fechaFin: "2026-09-12",
            cierreInscripcion: "2026-08-05",
            cupoMaximo: 8,
            cuposOcupados: 8,
            organizador: "RefereePro",
            banner: "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?auto=format&fit=crop&w=1200&q=80",
            descripcion: "La competencia definitiva de tiradores tacticos. Fase de grupos seguida por llaves de doble eliminacion.",
            requisitos: [
                "Escuadra con 5 jugadores titulares confirmados",
                "Uso obligatorio de cliente Riot Vanguard activo",
                "Comunicacion de voz via servidor oficial"
            ],
            premios: [
                { posicion: 1, recompensa: "$800.000 CLP + Pase a Circuito Regional" },
                { posicion: 2, recompensa: "$400.000 CLP + Perifericos Gaming" },
                { posicion: 3, recompensa: "$200.000 CLP" }
            ],
            inscritos: [
                { id: 201, participanteId: 21, nombre: "Viper Squad", tipo: "EQUIPO", fecha: "2026-07-28" },
                { id: 202, participanteId: 22, nombre: "Reyna Rushers", tipo: "EQUIPO", fecha: "2026-07-29" },
                { id: 203, participanteId: 23, nombre: "Phantom Force", tipo: "EQUIPO", fecha: "2026-08-01" },
                { id: 204, participanteId: 24, nombre: "Omen Syndicate", tipo: "EQUIPO", fecha: "2026-08-02" },
                { id: 205, participanteId: 25, nombre: "Brimstone Battalion", tipo: "EQUIPO", fecha: "2026-08-03" },
                { id: 206, participanteId: 26, nombre: "Jett Ascendant", tipo: "EQUIPO", fecha: "2026-08-04" },
                { id: 207, participanteId: 27, nombre: "Sova Scouters", tipo: "EQUIPO", fecha: "2026-08-04" },
                { id: 208, participanteId: 28, nombre: "Cypher Security", tipo: "EQUIPO", fecha: "2026-08-05" }
            ],
            partidas: [
                {
                    ronda: "Fase de Grupos - R1",
                    id: 11,
                    equipoA: "Viper Squad",
                    equipoB: "Reyna Rushers",
                    fechaHora: "2026-08-15 19:00",
                    estado: "FINALIZADA",
                    resultado: "13 - 9 (Victoria Viper Squad)"
                },
                {
                    ronda: "Fase de Grupos - R1",
                    id: 12,
                    equipoA: "Phantom Force",
                    equipoB: "Omen Syndicate",
                    fechaHora: "2026-08-15 21:00",
                    estado: "FINALIZADA",
                    resultado: "13 - 11 (Victoria Phantom Force)"
                },
                {
                    ronda: "Semifinales",
                    id: 13,
                    equipoA: "Viper Squad",
                    equipoB: "Phantom Force",
                    fechaHora: "2026-09-08 20:00",
                    estado: "PROGRAMADA",
                    resultado: null
                },
                {
                    ronda: "Gran Final",
                    id: 14,
                    equipoA: "Por definir",
                    equipoB: "Por definir",
                    fechaHora: "2026-09-12 21:30",
                    estado: "PROGRAMADA",
                    resultado: null
                }
            ],
            ranking: [
                { posicion: 1, participante: "Viper Squad", partidasJugadas: 3, victorias: 3, derrotas: 0, puntos: 9, difPuntos: +18 },
                { posicion: 2, participante: "Phantom Force", partidasJugadas: 3, victorias: 2, derrotas: 1, puntos: 6, difPuntos: +8 },
                { posicion: 3, participante: "Reyna Rushers", partidasJugadas: 3, victorias: 2, derrotas: 1, puntos: 6, difPuntos: +3 },
                { posicion: 4, participante: "Omen Syndicate", partidasJugadas: 3, victorias: 1, derrotas: 2, puntos: 3, difPuntos: -4 },
                { posicion: 5, participante: "Jett Ascendant", partidasJugadas: 3, victorias: 0, derrotas: 3, puntos: 0, difPuntos: -15 }
            ]
        },
        {
            id: 3,
            nombre: "Rocket League Octane Clash 3v3",
            juegoId: 3,
            juegoNombre: "Rocket League",
            modalidad: "3v3 Equipos",
            tipoParticipante: "EQUIPO",
            estado: "ABIERTO",
            fechaInicio: "2026-09-25",
            fechaFin: "2026-10-02",
            cierreInscripcion: "2026-09-22",
            cupoMaximo: 16,
            cuposOcupados: 6,
            organizador: "RefereePro",
            banner: "https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=1200&q=80",
            descripcion: "Velocidad, propulsion aerea y precision. Torneo relampago de fin de semana.",
            requisitos: [
                "Equipo con minimo 3 integrantes registrados",
                "Sin restriccion de plataforma (Cross-Play activo)",
                "Puntualidad en check-in 15 minutos previos"
            ],
            premios: [
                { posicion: 1, recompensa: "$350.000 CLP + 10.000 Creditos" },
                { posicion: 2, recompensa: "$180.000 CLP + 5.000 Creditos" },
                { posicion: 3, recompensa: "$90.000 CLP" }
            ],
            inscritos: [
                { id: 301, participanteId: 3, nombre: "Turbo Boosters", tipo: "EQUIPO", fecha: "2026-08-20" },
                { id: 302, participanteId: 32, nombre: "Aerial Kings", tipo: "EQUIPO", fecha: "2026-08-22" },
                { id: 303, participanteId: 33, nombre: "Flip Reseters", tipo: "EQUIPO", fecha: "2026-08-25" }
            ],
            partidas: [
                {
                    ronda: "Ronda 1",
                    id: 21,
                    equipoA: "Turbo Boosters",
                    equipoB: "Aerial Kings",
                    fechaHora: "2026-09-25 18:30",
                    estado: "PROGRAMADA",
                    resultado: null
                }
            ],
            ranking: [
                { posicion: 1, participante: "Turbo Boosters", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 },
                { posicion: 2, participante: "Aerial Kings", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 },
                { posicion: 3, participante: "Flip Reseters", partidasJugadas: 0, victorias: 0, derrotas: 0, puntos: 0, difPuntos: 0 }
            ]
        },
        {
            id: 4,
            nombre: "Street Fighter 6 Iron Fist Invitational",
            juegoId: 4,
            juegoNombre: "Street Fighter 6",
            modalidad: "1v1 Individual",
            tipoParticipante: "INDIVIDUAL",
            estado: "FINALIZADO",
            fechaInicio: "2026-07-01",
            fechaFin: "2026-07-15",
            cierreInscripcion: "2026-06-25",
            cupoMaximo: 16,
            cuposOcupados: 16,
            organizador: "RefereePro",
            banner: "https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=1200&q=80",
            descripcion: "Torneo de exhibicion de fighting games con los mejores duelistas de la region.",
            requisitos: [
                "Inscripcion individual de jugador",
                "Conexion por cable LAN obligatoria (sin Wi-Fi)",
                "Juego en plataforma PC o PlayStation 5"
            ],
            premios: [
                { posicion: 1, recompensa: "$300.000 CLP + Arcade Stick Pro Qanba" },
                { posicion: 2, recompensa: "$150.000 CLP + Auriculares HyperX" },
                { posicion: 3, recompensa: "$75.000 CLP" }
            ],
            inscritos: [
                { id: 401, participanteId: 1, nombre: "ShadowStriker", tipo: "INDIVIDUAL", fecha: "2026-06-20" },
                { id: 402, participanteId: 2, nombre: "Valkyria99", tipo: "INDIVIDUAL", fecha: "2026-06-21" },
                { id: 403, participanteId: 4, nombre: "CyberKnight", tipo: "INDIVIDUAL", fecha: "2026-06-22" },
                { id: 404, participanteId: 5, nombre: "PixelQueen", tipo: "INDIVIDUAL", fecha: "2026-06-23" }
            ],
            partidas: [
                {
                    ronda: "Gran Final",
                    id: 31,
                    equipoA: "ShadowStriker",
                    equipoB: "Valkyria99",
                    fechaHora: "2026-07-15 20:00",
                    estado: "FINALIZADA",
                    resultado: "3 - 2 (Campeon: ShadowStriker)"
                }
            ],
            ranking: [
                { posicion: 1, participante: "ShadowStriker", partidasJugadas: 5, victorias: 5, derrotas: 0, puntos: 15, difPuntos: +9 },
                { posicion: 2, participante: "Valkyria99", partidasJugadas: 5, victorias: 4, derrotas: 1, puntos: 12, difPuntos: +6 },
                { posicion: 3, participante: "CyberKnight", partidasJugadas: 4, victorias: 2, derrotas: 2, puntos: 6, difPuntos: +1 },
                { posicion: 4, participante: "PixelQueen", partidasJugadas: 4, victorias: 1, derrotas: 3, puntos: 3, difPuntos: -4 }
            ]
        }
    ]
};

// Funciones de consulta y persistencia en localStorage
const ArenaStorage = {
    init() {
        if (!localStorage.getItem("arena_torneos")) {
            localStorage.setItem("arena_torneos", JSON.stringify(DB.torneos));
        }
        if (!localStorage.getItem("arena_equipos")) {
            localStorage.setItem("arena_equipos", JSON.stringify(DB.equipos));
        }
        if (!localStorage.getItem("arena_usuarios")) {
            localStorage.setItem("arena_usuarios", JSON.stringify(DB.usuarios));
        }
        if (!localStorage.getItem("arena_juegos")) {
            localStorage.setItem("arena_juegos", JSON.stringify(DB.juegos));
        }
        if (!localStorage.getItem("arena_sesion_usuario")) {
            localStorage.setItem("arena_sesion_usuario", JSON.stringify(DB.usuarios[0]));
        }
    },

    getTorneos() {
        this.init();
        return JSON.parse(localStorage.getItem("arena_torneos"));
    },

    getTorneoById(id) {
        const torneos = this.getTorneos();
        return torneos.find(t => t.id === parseInt(id, 10));
    },

    saveTorneos(torneos) {
        localStorage.setItem("arena_torneos", JSON.stringify(torneos));
    },

    getEquipos() {
        this.init();
        return JSON.parse(localStorage.getItem("arena_equipos"));
    },

    saveEquipos(equipos) {
        localStorage.setItem("arena_equipos", JSON.stringify(equipos));
    },

    getUsuarios() {
        this.init();
        return JSON.parse(localStorage.getItem("arena_usuarios"));
    },

    saveUsuarios(usuarios) {
        localStorage.setItem("arena_usuarios", JSON.stringify(usuarios));
    },

    getJuegos() {
        this.init();
        return JSON.parse(localStorage.getItem("arena_juegos"));
    },

    getUsuarioActual() {
        this.init();
        return JSON.parse(localStorage.getItem("arena_sesion_usuario"));
    },

    setUsuarioActual(usuario) {
        localStorage.setItem("arena_sesion_usuario", JSON.stringify(usuario));
    },

    calcularCuposDisponibles(torneo) {
        const disponibles = (torneo.cupoMaximo || 0) - (torneo.cuposOcupados || 0);
        return Math.max(0, disponibles);
    },

    esInscripcionFueraDePlazo(cierreInscripcionStr) {
        const fechaActual = new Date();
        const fechaCierre = new Date(cierreInscripcionStr + "T23:59:59");
        return fechaActual > fechaCierre;
    },

    tieneSancionActiva(usuarioId) {
        const usuarios = this.getUsuarios();
        const user = usuarios.find(u => u.id === parseInt(usuarioId, 10));
        if (!user || !user.sanciones) return { activa: false };

        const sancionVigente = user.sanciones.find(s => s.estado === "VIGENTE");
        if (sancionVigente) {
            return {
                activa: true,
                motivo: sancionVigente.motivo,
                fechaFin: sancionVigente.fechaFin
            };
        }
        return { activa: false };
    }
};

// Inicializar al cargar
ArenaStorage.init();
