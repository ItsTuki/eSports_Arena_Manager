# eSports Arena Manager

Asignatura: Desarrollo FullStack I / Frontend DSY1103  
Institución: DuocUC  
Arquitectura: Microservicios Spring Boot 3.2.5 + Java 21 + Frontend React 18 / HTML5

## Integrantes del equipo

| Nombre          | Rol | GitHub |
|-----------------|-----|--------|
| _Anibal Romero_ | FullStack Dev | @ItsTuki |
| _Victor Guerra_ | FullStack Dev | @jazinto-Flores |
| _Maximo Lugo_   | FullStack Dev | @Tynx006 |

---

## 🎮 Capa Frontend (EP1, EP2, EP3)

El proyecto cuenta con una capa de presentación completa integrada que cubre las tres evaluaciones parciales:
- **EP1:** Base web con HTML5 semántico, CSS3 externo (Paleta 1 Arena Púrpura `#9146FF`), validaciones JavaScript y catálogo dinámico por DOM (`frontend/ep1-base/index.html`).
- **EP2:** Migración a React 18, componentes modulares con propiedades y estados (`TarjetaTorneo`, `TablaRanking`, `ListaPartidas`, `LlaveTorneo`, `FormularioInscripcion`), diseño responsivo con Bootstrap 5 (360px, 768px, 1280px), y suite de 10 pruebas unitarias con Jasmine/Karma (`frontend/tests/SpecRunner.html`).
- **EP3:** Integración con el ecosistema de microservicios a través del **API Gateway (puerto 8070)**, autenticación con token JWT en cabecera `Authorization: Bearer`, persistencia en `localStorage`, control de acceso por roles (`ADMINISTRADOR`, `ORGANIZADOR`, `JUGADOR`, `VISITANTE`), y manejo de errores HTTP (400, 401, 403, 404, 500).

### Vistas Disponibles en la Aplicación Frontend:
1. **Inicio (`/`):** Torneos destacados, video oficial, métricas de arena y próximos cierres.
2. **Listado de Torneos (`/torneos`):** Buscador, filtros por juego, estado y rango de fechas con estado vacío explícito.
3. **Detalle de Torneo (`/torneos/:id`):** Llaves de eliminación (`LlaveTorneo`), tabla de posiciones (`TablaRanking`), calendario de partidas y asignación de premios.
4. **Inscripción a Torneo (`/inscripcion`):** Formulario controlado con validación de plazo, cupo, jugadores mínimos del juego, duplicidad y sanciones activas.
5. **Gestión de Equipos (`/equipos`):** Creación de escuadras con capitán y administración de plantilla de jugadores con roles tácticos.
6. **Perfil de Jugador (`/perfil`):** Ficha técnica, apodo sin espacios (3-20 caracteres), historial y ratio de victorias/derrotas.
7. **Autenticación (`/auth`):** Inicio de sesión con JWT y registro con mensajes diferenciados.
8. **Panel del Organizador (`/organizador`):** Aprobación de inscripciones, registro/validación de resultados y aplicación de sanciones.
9. **Panel de Administración (`/admin`):** Mantenedor CRUD de juegos, creación de torneos y configuración de premios.

### Documentos Entregables del Caso Frontend:
- [ERS - Especificación de Requerimientos de Software](ERS_eSports_Arena_Manager.md)
- [Documento de Cobertura de Testing Unitario](Documento_Cobertura_Testing.md)
- [Documento de Integración API y Microservicios](Documento_Integracion_API.md)
- [Manual de Usuario por Rol](Manual_de_Usuario.md)

---

## Mapa de microservicios y puertos

| Servicio             | Puerto | Base de datos      | MySQL puerto |
|----------------------|--------|--------------------|-------------|
| `discovery-server`   | 8761   | No aplica          | No aplica   |
| `api-gateway`        | 8070   | No aplica          | No aplica   |
| `auth-service`       | 8080   | `db_auth`          | 3306        |
| `user-service`       | 8081   | `db_users`         | 3306        |
| `team-service`       | 8082   | `db_teams`         | 3306        |
| `tournament-service` | 8083   | `db_tournaments`   | 3306        |
| `registration-service` | 8084 | `db_registration`  | 3306        |
| `sanction-service`   | 8085   | `db_sanctions`     | 3306        |
| `match-service`      | 8086   | `db_matches`       | 3306        |
| `game-service`       | 8087   | `db_games`         | 3306        |
| `result-service`     | 8088   | `db_results`       | 3306        |
| `ranking-service`    | 8089   | `db_rankings`      | 3306        |
| `prize-service`      | 8090   | `db_prizes`        | 3306        |
| `notification-service` | 8091 | `db_notifications` | 3306        |

## Documentación Swagger / OpenAPI

Cada microservicio expone su documentación Swagger UI en la ruta `/doc/swagger-ui/index.html`.

| Servicio             | Swagger UI |
|----------------------|------------|
| `auth-service`       | `http://localhost:8080/doc/swagger-ui/index.html` |
| `user-service`       | `http://localhost:8081/doc/swagger-ui/index.html` |
| `team-service`       | `http://localhost:8082/doc/swagger-ui/index.html` |
| `tournament-service` | `http://localhost:8083/doc/swagger-ui/index.html` |
| `registration-service` | `http://localhost:8084/doc/swagger-ui/index.html` |
| `sanction-service`   | `http://localhost:8085/doc/swagger-ui/index.html` |
| `match-service`      | `http://localhost:8086/doc/swagger-ui/index.html` |
| `game-service`       | `http://localhost:8087/doc/swagger-ui/index.html` |
| `result-service`     | `http://localhost:8088/doc/swagger-ui/index.html` |
| `ranking-service`    | `http://localhost:8089/doc/swagger-ui/index.html` |
| `prize-service`      | `http://localhost:8090/doc/swagger-ui/index.html` |
| `notification-service` | `http://localhost:8091/doc/swagger-ui/index.html` |

---

## Instrucciones de Ejecución

### 1. Ejecutar el Frontend
Abrir directamente en el navegador:
- **Aplicación React (SPA):** `frontend/index.html`
- **Suite de Pruebas Jasmine (EP2):** `frontend/tests/SpecRunner.html`
- **Base HTML5/CSS3/JS (EP1):** `frontend/ep1-base/index.html`

### 2. Ejecutar los Microservicios
1. Iniciar el servicio **MySQL** en XAMPP (puerto 3306).
2. Iniciar Eureka Server:
   ```bash
   .\mvnw.cmd -pl discovery-server spring-boot:run
   ```
3. Iniciar los microservicios (`auth-service`, `user-service`, etc.).
4. Iniciar el API Gateway:
   ```bash
   .\mvnw.cmd -pl api-gateway spring-boot:run
   ```
