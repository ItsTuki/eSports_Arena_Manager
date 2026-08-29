# Especificación de Requerimientos de Software (ERS)
## eSports Arena Manager - Capa de Presentación

**Asignatura:** Desarrollo FullStack / Frontend  
**Institución:** DuocUC  
**Equipo Desarrollador:**
- Anibal Romero (@ItsTuki)
- Victor Guerra (@jazinto-Flores)
- Maximo Lugo (@Tynx006)

---

## 1. Introducción y Propósito
El sistema **eSports Arena Manager** es una plataforma integral para la gestión y seguimiento de torneos de deportes electrónicos competitivos. Permite a administradores configurar juegos y crear torneos, a organizadores validar inscripciones, generar llaves, programar partidas, registrar resultados y aplicar sanciones, y a jugadores formar escuadras, inscribirse y seguir su rendimiento en tiempo real.

---

## 2. Requerimientos Funcionales (RF)

| ID | Nombre | Descripción | Rol |
|---|---|---|---|
| **RF01** | Visualización de Torneos Destacados | Presentar torneos en curso, próximos cierres, video promocional y métricas generales. | Público |
| **RF02** | Filtrado y Búsqueda de Torneos | Filtrar por juego habilitado, estado (Abierto, En Curso, Finalizado) y rango de fechas con control de errores. | Público |
| **RF03** | Detalle Integral de Torneo | Desplegar llaves (`LlaveTorneo`), tabla de posiciones (`TablaRanking`), calendario de partidas y bolsa de premios. | Público |
| **RF04** | Inscripción a Torneo con Validación | Validar en tiempo real: plazo de cierre, cupos disponibles, integrantes mínimos según el juego, ausencia de sanciones activas y duplicidad. | Jugador |
| **RF05** | Gestión de Equipos | Crear equipos con nombre único, asignar capitán, agregar y eliminar integrantes con roles tácticos específicos. | Jugador |
| **RF06** | Ficha de Perfil de Jugador | Consultar y actualizar apodo (sin espacios, 3-20 caracteres), datos de contacto, historial de torneos, ratio victorias/derrotas y sanciones. | Jugador |
| **RF07** | Autenticación y Autorización JWT | Iniciar sesión y registro con token JWT, almacenamiento seguro en `localStorage` y redirección condicional por rol. | Todos |
| **RF08** | Panel Operativo del Organizador | Bandeja de inscripciones para aprobación, generador de llaves/partidas, registro de marcadores validados y aplicación de sanciones disciplinarias. | Organizador / Admin |
| **RF09** | Panel de Configuración Administrativa | Mantenedor CRUD de juegos habilitados, creación de torneos oficiales y asignación de premios por posición. | Administrador |

---

## 3. Requerimientos No Funcionales (RNF)

- **RNF01 - Semántica y Accesibilidad:** Estructura en HTML5 semántico (`header`, `nav`, `main`, `section`, `article`, `footer`), contraste WCAG AA (>= 4.5:1 en texto normal, >= 3:1 en títulos), atributos `alt` y labels enlazados.
- **RNF02 - Identidad Visual y Paleta de Colores:** Implementación de la **Paleta 1 (Arena Púrpura)**:
  - Fondo: `#0E0B16`
  - Superficie: `#1C1430`
  - Primario: `#9146FF` (Twitch Violet)
  - Acento: `#00F5D4` (Turquesa de validación)
  - Texto: `#F1ECFF`
  - Error: `#FF5C7A`
  - Radio de borde: `8px`
- **RNF03 - Diseño Responsivo:** Adaptación fluida comprobada en resoluciones de 360px (móvil), 768px (tablet) y 1280px (escritorio) mediante Bootstrap 5 y Flexbox/CSS Grid.
- **RNF04 - Arquitectura y Componentización:** Componentes React reutilizables con gestión de estado local (`useState`), efectos (`useEffect`) y memorización (`useMemo`).
- **RNF05 - Cobertura de Pruebas Automatizadas:** 10 pruebas unitarias con Jasmine y Karma para verificar lógica de negocio y comportamiento de componentes en el DOM.
- **RNF06 - Integración REST API:** Comunicación desacoplada con el API Gateway en puerto 8070, manejo uniforme de errores HTTP (400, 401, 403, 404, 500) y fallback de datos simulados para resiliencia.

---

## 4. Matriz de Trazabilidad de Vistas y Componentes

| Vista | Ruta | Componentes Clave | Servicios Consumidos |
|---|---|---|---|
| **Inicio** | `/` | `Cabecera`, `TarjetaTorneo`, `PieDePagina` | `tournament-service` |
| **Listado de Torneos** | `/torneos` | `TarjetaTorneo`, `EstadoVacio`, `Filtros` | `tournament-service`, `game-service` |
| **Detalle de Torneo** | `/torneos/:id` | `LlaveTorneo`, `TablaRanking`, `ListaPartidas` | `tournament-service`, `registration-service`, `match-service`, `result-service`, `ranking-service`, `prize-service` |
| **Inscripción** | `/inscripcion` | `FormularioInscripcion`, `AlertaValidacion` | `registration-service`, `sanction-service`, `team-service` |
| **Gestión de Equipos** | `/equipos` | `FormularioEquipo`, `TablaMiembros` | `team-service`, `user-service` |
| **Perfil Jugador** | `/perfil` | `TarjetaPerfil`, `HistorialSanciones` | `user-service`, `sanction-service`, `result-service` |
| **Autenticación** | `/login` / `/registro` | `FormularioAuth` | `auth-service` (JWT) |
| **Panel Organizador** | `/organizador` | `BandejaInscripciones`, `FormResultado`, `FormSancion` | `registration-service`, `match-service`, `result-service`, `sanction-service` |
| **Panel Admin** | `/admin` | `MantenedorJuegos`, `FormTorneo`, `FormPremios` | `game-service`, `tournament-service`, `prize-service` |
