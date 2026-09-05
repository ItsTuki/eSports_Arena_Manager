# Especificacion de Requerimientos de Software (ERS)
## eSports Arena Manager - Version 1.0 (EP1)

**Asignatura:** Desarrollo FullStack II (DSY1104)  
**Institucion:** Duoc UC  
**Integrantes:**
- Anibal Romero
- Victor Guerra
- Maximo Lugo

---

## 1. Introduccion y Objetivos

### 1.1 Proposito del documento
Este documento define los requerimientos funcionales y no funcionales para la primera entrega (EP1) del proyecto frontend **eSports Arena Manager**. La plataforma permite administrar torneos de videojuegos competitivos, equipos, inscripciones, partidas y clasificaciones.

### 1.2 Alcance de la Evaluacion Parcial 1 (30%)
Para la EP1 se implementa la base web del sistema utilizando **HTML5 semantico, CSS3 y JavaScript vanilla** (sin frameworks y sin backend). Los datos se manejan mediante estructuras simuladas en memoria y `localStorage`.

---

## 2. Descripcion General del Sistema

### 2.1 Roles de usuario
- **Administrador:** Configura juegos habilitados, crea y cierra torneos, define premios y reglas.
- **Organizador:** Gestiona inscripciones, programa partidas, valida resultados y aplica sanciones.
- **Jugador:** Crea y administra equipos, se inscribe a torneos y consulta su historial y estadísticas.
- **Visitante:** Explora torneos publicos, llaves de partidas y tablas de posiciones sin iniciar sesion.

En EP1 los roles se simulan mediante un selector de perfiles de prueba en la barra de navegacion.

### 2.2 Paleta de colores seleccionada
Se selecciono la **Paleta 1: Arena Purpura** indicada en la guia:
- Fondo: `#0E0B16`
- Superficie: `#1C1430`
- Primario: `#9146FF`
- Acento: `#00F5D4`
- Texto: `#F1ECFF`
- Error: `#FF5C7A`
- Radio de borde: `8px`

---

## 3. Requerimientos Funcionales

### Vista 1: Inicio (index.html)
- **RF01:** Barra de navegacion superior accesible con enlaces a todas las secciones y selector de usuario de prueba.
- **RF02:** Seccion de torneos destacados generada dinamicamente con JavaScript a partir de los datos simulados.
- **RF03:** Bloque de proximos cierres de inscripcion ordenados por fecha limite.
- **RF04:** Video embebido con controles y diseno responsivo.
- **RF05:** Footer con informacion del proyecto y enlaces de navegacion.

### Vista 2: Listado de Torneos (torneos.html)
- **RF06:** Buscador en tiempo real por nombre de torneo o juego.
- **RF07:** Filtros combinables por juego, estado (Abierto, En Curso, Finalizado) y rango de fechas.
- **RF08:** Validacion de fechas: la fecha inicial no puede ser posterior a la fecha final.
- **RF09:** Mensaje de estado vacio explicito cuando no existan coincidencias con los filtros.

### Vista 3: Detalle de Torneo (detalle-torneo.html)
- **RF10:** Carga de torneo por parametro de URL (?id=X) o selector desplegable.
- **RF11:** Informacion general del torneo: juego, fechas, organizador, requisitos y barra de cupos disponibles.
- **RF12:** Lista de participantes inscritos con fecha de registro.
- **RF13:** Llaves de partidas con pestanas interactivas para filtrar por ronda.
- **RF14:** Tabla de posiciones ordenada por puntos y diferencia de puntaje, destacando el podio.
- **RF15:** Seccion de premios asignados o en disputa.

### Vista 4: Inscripcion a Torneo (inscripcion.html)
- **RF16:** Formulario principal con etiquetas asociadas (label for), autocompletado y textos de ayuda.
- **RF17:** Validacion: bloqueo de inscripcion si la fecha supera el cierre del torneo.
- **RF18:** Validacion: bloqueo si se alcanza el cupo maximo de participantes.
- **RF19:** Validacion: no permitir inscribir dos veces al mismo participante en el mismo torneo.
- **RF20:** Validacion: verificar que el equipo cumpla con el minimo de integrantes exigido por el juego.
- **RF21:** Validacion: bloqueo si el participante o algun integrante tiene sancion disciplinaria activa.
- **RF22:** Validacion de formato de correo electronico y checkbox de aceptacion del reglamento.
- **RF23:** Mensajes de error junto al campo correspondiente y bloqueo de boton de envio si hay errores.

### Vista 5: Gestion de Equipos (equipos.html)
- **RF24:** Formulario de creacion de equipo con nombre, juego principal y capitan.
- **RF25:** Validacion de nombre obligatorio y sin duplicados en los equipos existentes.
- **RF26:** Panel interactivo para anadir y quitar integrantes asignando roles (Capitan, Titular, Suplente).
- **RF27:** Validacion: un mismo jugador no puede repetirse dentro del equipo.
- **RF28:** Listado de equipos registrados con su plantilla de jugadores.

### Vista 6: Perfil de Jugador (perfil.html)
- **RF29:** Ficha de jugador con apodo, avatar, rango y calculo automatico de porcentaje de victorias (winrate).
- **RF30:** Listado de equipos a los que pertenece el usuario e historial de torneos disputados.
- **RF31:** Visualizacion de sanciones vigentes y cumplidas con motivo y fechas.
- **RF32:** Formulario de edicion de datos con validacion de apodo obligatorio sin espacios y largo entre 3 y 20 caracteres.

---

## 4. Requerimientos No Funcionales

- **RNF01 (Semantica HTML5):** Uso correcto de etiquetas semanticas (header, nav, main, section, article, footer, label) y accesibilidad con textos alternativos en imagenes.
- **RNF02 (Estilos CSS externos):** Hoja de estilos externa unica (styles.css) utilizando variables CSS y maquetacion con Flexbox y CSS Grid.
- **RNF03 (Contraste visual):** Cumplimiento de contraste minimo de 4.5:1 para texto normal y botones con texto mayor o igual a 16px.
- **RNF04 (Diseno responsivo):** Adaptabilidad en pantallas de escritorio, tabletas y dispositivos moviles.
- **RNF05 (Persistencia en cliente):** Uso de localStorage para mantener las inscripciones, equipos y perfiles actualizados durante la navegacion.
- **RNF06 (Sin librerias externas):** Implementacion en JavaScript nativo (DOM manipulation) sin frameworks.

---

## 5. Estructura del Proyecto Frontend

```text
frontend/
├── index.html
├── torneos.html
├── detalle-torneo.html
├── inscripcion.html
├── equipos.html
├── perfil.html
├── css/
│   └── styles.css
├── js/
│   ├── data.js
│   ├── app.js
│   ├── inicio.js
│   ├── torneos.js
│   ├── detalle-torneo.js
│   ├── inscripcion.js
│   ├── equipos.js
│   └── perfil.js
└── ERS_v1.md
```
