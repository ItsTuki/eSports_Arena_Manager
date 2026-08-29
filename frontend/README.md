# eSports Arena Manager - Frontend

Aplicación frontend para la plataforma de gestión de torneos de deportes electrónicos **eSports Arena Manager**, desarrollada cumpliendo las etapas **EP1**, **EP2** y **EP3** del caso semestral.

## Integrantes del Equipo

- **Anibal Romero** (@ItsTuki)
- **Victor Guerra** (@jazinto-Flores)
- **Maximo Lugo** (@Tynx006)

---

## 🎨 Identidad Visual y Paleta de Colores
Se implementó la **Paleta 1: Arena Púrpura**:
- **Fondo:** `#0E0B16`
- **Superficie:** `#1C1430`
- **Primario:** `#9146FF` (Púrpura Twitch)
- **Acento:** `#00F5D4` (Turquesa de validación)
- **Texto:** `#F1ECFF`
- **Error:** `#FF5C7A`
- **Radio de Borde:** `8px`

---

## 🚀 Estructura del Proyecto Frontend

```text
frontend/
├── index.html                   # Aplicación Single Page App en React 18 con Bootstrap 5
├── package.json                 # Configuración de dependencias y scripts de prueba
├── karma.conf.js                # Configuración del ejecutor de pruebas Karma
├── css/
│   └── styles.css               # Hoja de estilos con variables de la Paleta 1
├── js/
│   ├── api.js                   # Cliente REST API hacia API Gateway (Puerto 8070)
│   ├── authService.js           # Gestión de autenticación JWT y persistencia de sesión
│   ├── mockData.js              # Datos simulados con esquema de los 12 microservicios
│   ├── tournamentUtils.js       # Reglas de negocio del dominio (ordenar, puntos, cupos, etc.)
│   └── validationUtils.js       # Validaciones de formularios del cliente
├── ep1-base/                    # Base HTML5 semántico puro + CSS3 + JS nativo (EP1)
│   ├── index.html               # Inicio con video embebido y catálogo JS
│   ├── torneos.html             # Listado de torneos con manipulación del DOM
│   ├── detalle-torneo.html      # Ficha técnica y posiciones
│   ├── inscripcion.html         # Formulario con validaciones en JavaScript
│   ├── equipo.html              # Creación de escuadras y plantilla
│   ├── perfil.html              # Ficha de jugador y estadísticas
│   ├── css/ep1-styles.css       # Hoja de estilos externa EP1
│   └── js/                      # Lógica de datos y validaciones EP1
└── tests/                       # Suite de Pruebas Unitarias (EP2)
    ├── SpecRunner.html          # Ejecutor de pruebas interactivo en navegador
    └── specs/
        ├── tournamentUtils.spec.js # Pruebas 1 al 6 (Lógica de negocio)
        └── components.spec.js      # Pruebas 7 al 10 (Componentes y espías)
```

---

## 🧪 Ejecución de Pruebas Unitarias (EP2)

Se incluyen las **10 pruebas obligatorias** con Jasmine:
1. `ordenarRanking`: Ordena por puntos y desempata por diferencia.
2. `calcularPuntos`: Asigna puntos por victoria (+3), empate (+1) y derrota (+0).
3. `cuposDisponibles`: Resta inscripciones al cupo sin devolver negativos.
4. `inscripcionFueraDePlazo`: Detecta si la fecha actual supera el cierre.
5. `tieneSancionActiva`: Bloquea participantes sancionados.
6. `equipoCompleto`: Valida cantidad de miembros según el juego.
7. `TarjetaTorneo`: Renderizado correcto de datos del torneo.
8. `TablaRanking`: Renderizado de filas respetando orden.
9. `FormularioInscripcion`: Bloqueo de botón con espía (`jasmine.createSpy`).
10. `LlaveTorneo`: Texto "Por definir" en partidas sin rival.

### Cómo ejecutar las pruebas:
- **En el navegador:** Abrir directamente `frontend/tests/SpecRunner.html`.

---

## 🔌 Conexión con Microservicios (EP3)

El frontend se conecta a través del **API Gateway** en `http://localhost:8070` enviando el token JWT en la cabecera `Authorization: Bearer <token>`.
Si el backend no está iniciado, la aplicación conmuta automáticamente al modo de simulación y fallback de datos para permitir una navegación fluida.
