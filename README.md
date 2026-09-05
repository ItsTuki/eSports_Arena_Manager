# eSports Arena Manager

**Institución:** Duoc UC  
**Carrera:** Ingeniería en Informática / Desarrollo de Aplicaciones  
**Asignaturas del Proyecto:**
- Desarrollo FullStack I (`DSY1103`): Ecosistema de Microservicios Backend con Spring Boot 3.2.5 + Java 21.
- Desarrollo FullStack II (`DSY1104`): Capa de Presentación Frontend (EP1 Base Web, EP2 React + Testing, EP3 Integración).

## Integrantes del equipo

| Nombre | Rol | GitHub |
|---|---|---|
| _Aníbal Romero_ | FullStack Dev | [@ItsTuki](https://github.com/ItsTuki) |
| _Víctor Guerra_ | FullStack Dev | [@jazinto-Flores](https://github.com/jazinto-Flores) |
| _Máximo Lugo_   | FullStack Dev | [@Tynx006](https://github.com/Tynx006) |

---

# Capa Frontend - Evaluación Parcial 1 (EP1 - 30%)

La entrega de **Evaluación Parcial 1 (EP1)** comprende la base web navegable e interactiva desarrollada con **HTML5 Semántico, CSS3 (Paleta 1: Arena Púrpura) y JavaScript puro (Vanilla JS)**, sin dependencias de frameworks ni conexión al backend.

### Vistas Implementadas (Interconectadas)
1. **Inicio (`frontend/index.html`):** Presentación de plataforma, torneos destacados generados dinámicamente vía DOM, próximos cierres de convocatoria ordenados por proximidad, video embebido responsivo y footer informativo.
2. **Catálogo de Torneos (`frontend/torneos.html`):** Filtro por disciplina/juego, estado (`ABIERTO`, `EN_CURSO`, `FINALIZADO`), rango de fechas con validación de coherencia (fecha inicio menor o igual a fecha fin), buscador en tiempo real y manejo de estado vacío explícito.
3. **Detalle de Torneo (`frontend/detalle-torneo.html`):** Banner, metadatos, requisitos, barra de cupos con cálculo dinámico, lista de participantes inscritos, calendario/llaves con selector de rondas, tabla de clasificación (*Ranking*) y bolsa de premios.
4. **Formulario de Inscripción (`frontend/inscripcion.html`):** Formulario principal del dominio con etiquetas asociadas `label for`, `autocomplete`, textos de ayuda y **validaciones de negocio en cliente**:
   - Bloqueo de inscripción fuera de plazo (`cierreInscripcion`).
   - Bloqueo por cupo máximo alcanzado.
   - Bloqueo de participantes duplicados en un mismo torneo.
   - Verificación de integrantes mínimos según juego (ej. LoL 5v5, Valorant 5v5, Rocket League 3v3).
   - Bloqueo por sanciones disciplinarias vigentes con mensaje explicativo.
   - Validación de formato de correo y términos de fair play.
5. **Gestión de Equipos (`frontend/equipos.html`):** Formulario para registrar escuadras con nombre único, disciplina y capitán. Añadido y eliminación dinámica de integrantes con asignación de rol (Capitán, Titular, Suplente) e impedimento de jugadores duplicados.
6. **Perfil de Jugador (`frontend/perfil.html`):** Ficha competitiva con avatar, apodo, cálculo dinámico de winrate (Victorias / Partidas Totales), equipos asociados, historial de torneos, detalle de sanciones y edición de perfil con validación de apodo (sin espacios, entre 3 y 20 caracteres).

### Paleta de Colores Oficial (Paleta 1: Arena Púrpura)
- **Fondo:** `#0E0B16`
- **Superficie:** `#1C1430`
- **Primario:** `#9146FF` (Ratio de contraste 4.6:1)
- **Acento:** `#00F5D4`
- **Texto:** `#F1ECFF`
- **Error:** `#FF5C7A`

### Instrucciones de Ejecución del Frontend (EP1)
El frontend no requiere compilación ni instalación de paquetes Node.js:
1. Abrir la carpeta `frontend/`.
2. Hacer doble clic o abrir `frontend/index.html` en cualquier navegador web moderno (Google Chrome, Firefox, Microsoft Edge).
3. Alternativamente, utilizar la extensión **Live Server** de VS Code sobre la carpeta `frontend/`.
4. Usar el selector **Rol** en la barra superior para alternar entre diferentes perfiles de prueba (*ShadowStriker*, *Valkyria99*, *ToxicRage*, etc.).

---

# Capa Backend - Ecosistema de Microservicios (FullStack I / EP3)

## Mapa de microservicios y puertos

| Servicio | Puerto | Base de datos | MySQL puerto |
|---|---|---|---|
| `discovery-server` | 8761 | No aplica | No aplica |
| `api-gateway` | 8070 | No aplica | No aplica |
| `auth-service` | 8080 | `db_auth` | 3306 |
| `user-service` | 8081 | `db_users` | 3306 |
| `team-service` | 8082 | `db_teams` | 3306 |
| `tournament-service` | 8083 | `db_tournaments` | 3306 |
| `registration-service` | 8084 | `db_registration` | 3306 |
| `sanction-service` | 8085 | `db_sanctions` | 3306 |
| `match-service` | 8086 | `db_matches` | 3306 |
| `game-service` | 8087 | `db_games` | 3306 |
| `result-service` | 8088 | `db_results` | 3306 |
| `ranking-service` | 8089 | `db_rankings` | 3306 |
| `prize-service` | 8090 | `db_prizes` | 3306 |
| `notification-service` | 8091 | `db_notifications` | 3306 |

## Documentación Swagger / OpenAPI

Cada microservicio expone su documentación Swagger UI en la ruta `/doc/swagger-ui/index.html`.

| Servicio | Swagger UI |
|---|---|
| `auth-service` | `http://localhost:8080/doc/swagger-ui/index.html` |
| `user-service` | `http://localhost:8081/doc/swagger-ui/index.html` |
| `team-service` | `http://localhost:8082/doc/swagger-ui/index.html` |
| `tournament-service` | `http://localhost:8083/doc/swagger-ui/index.html` |
| `registration-service` | `http://localhost:8084/doc/swagger-ui/index.html` |
| `sanction-service` | `http://localhost:8085/doc/swagger-ui/index.html` |
| `match-service` | `http://localhost:8086/doc/swagger-ui/index.html` |
| `game-service` | `http://localhost:8087/doc/swagger-ui/index.html` |
| `result-service` | `http://localhost:8088/doc/swagger-ui/index.html` |
| `ranking-service` | `http://localhost:8089/doc/swagger-ui/index.html` |
| `prize-service` | `http://localhost:8090/doc/swagger-ui/index.html` |
| `notification-service` | `http://localhost:8091/doc/swagger-ui/index.html` |

## Instrucciones de ejecución del Backend

### Requisito de base de datos local
Para probar el backend localmente se utiliza **XAMPP** con el servicio **MySQL** activo en el puerto `3306`.

### Ejecución de microservicios
1. Abrir XAMPP e iniciar el servicio **MySQL**.
2. Iniciar el servidor Eureka:
   ```powershell
   .\mvnw.cmd -pl discovery-server spring-boot:run
   ```
3. Ejecutar los microservicios en el orden recomendado:
   `user-service`, `auth-service`, `game-service`, `tournament-service`, `team-service`, `sanction-service`, `registration-service`, `match-service`, `result-service`, `ranking-service`, `prize-service`, `notification-service`.
4. Iniciar el Gateway:
   ```powershell
   .\mvnw.cmd -pl api-gateway spring-boot:run
   ```

---

## Estructura del Repositorio

```text
eSports_Arena_Manager/
├── frontend/                          # Capa de Presentación (EP1)
│   ├── index.html                     # Vista 1: Inicio
│   ├── torneos.html                   # Vista 2: Catálogo y Búsqueda
│   ├── detalle-torneo.html            # Vista 3: Detalle, Partidas y Ranking
│   ├── inscripcion.html               # Vista 4: Inscripción oficial (Formulario dominio)
│   ├── equipos.html                   # Vista 5: Gestión de Escuadras
│   ├── perfil.html                    # Vista 6: Ficha de Jugador
│   ├── css/
│   │   └── styles.css                 # Estilos CSS externos (Paleta 1 Arena Púrpura)
│   ├── js/
│   │   ├── data.js                    # Base de datos simulada y reglas de negocio
│   │   ├── app.js                     # Utilidades globales y selector de perfiles
│   │   ├── inicio.js                  # Lógica de inicio y torneos destacados
│   │   ├── torneos.js                 # Lógica de catálogo y filtrado
│   │   ├── detalle-torneo.js          # Lógica de detalle, rondas y posiciones
│   │   ├── inscripcion.js             # Lógica de validaciones de inscripción
│   │   ├── equipos.js                 # Lógica de gestión de miembros
│   │   └── perfil.js                  # Lógica de perfil y estadísticas
│   └── ERS_v1.md                      # Especificación de Requerimientos de Software v1
├── api-gateway/                       # Microservicios Spring Boot
├── auth-service/
├── user-service/
├── team-service/
├── tournament-service/
├── registration-service/
├── sanction-service/
├── match-service/
├── game-service/
├── result-service/
├── ranking-service/
├── prize-service/
├── notification-service/
├── discovery-server/
└── README.md
```

## Evidencias de Entrega
- [x] Repositorio organizado con frontend y microservicios.
- [x] 6 vistas navegables implementadas con HTML5 semántico, CSS3 y JavaScript DOM.
- [x] Formulario principal del dominio con validación en cliente y feedback accesible.
- [x] Documento ERS Versión 1 (`frontend/ERS_v1.md`).
- [x] `README.md` actualizado con instrucciones y roles de equipo.
