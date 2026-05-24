# eSports Arena Manager – Backend Microservicios

> **Asignatura:** Desarrollo FullStack I DSY1103  
> **Institución:** DuocUC  
> **Arquitectura:** Microservicios con Spring Boot 3.2.5 + Java 21

---

## Integrantes del equipo

| Nombre          | Rol | GitHub |
|-----------------|-----|--------|
| _Anibal Romero_ | Backend Dev | @ItsTuki |
| _Victor Guerra_ | Backend Dev | @jazinto-Flores |
| _Maximo Lugo_   | Backend Dev | @Tynx006|

---

## Mapa de microservicios y puertos

| Servicio             | Puerto | Base de datos      | MySQL puerto |
|----------------------|--------|--------------------|-------------|
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

---

auth-service ──────────────────────────► user-service
                                              ▲
game-service ◄──── tournament-service         │
     ▲                    ▲              team-service
     │                    │                  ▲
     └────── team-service  │                 │
                           │         registration-service
                    match-service ──────────►│
                           │         sanction-service
                           ▼
                    result-service
                     │        │
                     ▼        ▼
               ranking-service  prize-service
                                     │
                              notification-service

---

## Flujo integrador principal


1. Admin registra juego (game-service)
2. Admin crea torneo asociado al juego (tournament-service)
3. Jugadores crean equipos (team-service)
4. registration-service valida cupo + sanciones + estado torneo → inscribe
5. match-service genera partidas entre inscritos
6. result-service registra y valida resultados
7. ranking-service recalcula posiciones
8. prize-service asigna premios → notification-service notifica


---

## Instrucciones de ejecución

### Requisito de base de datos local

Para probar el proyecto localmente se usa **XAMPP** con el servicio **MySQL/MariaDB** activo.

Configuración esperada por defecto:

```text
Host: localhost
Puerto MySQL: 3306
Usuario: root
Contraseña: vacía
```

Cada microservicio mantiene su propia base de datos independiente, pero todas usan el mismo servidor MySQL de XAMPP en el puerto `3306`.

Las bases son:

```text
db_auth
db_users
db_teams
db_games
db_tournaments
db_registration
db_sanctions
db_matches
db_results
db_rankings
db_prizes
db_notifications
```

Los `application.properties` usan `createDatabaseIfNotExist=true`, por lo que Hibernate puede crear las bases si el usuario `root` tiene permisos. Si XAMPP tiene contraseña configurada, definir:

```text
DB_USERNAME=root
DB_PASSWORD=tu_clave
```

### Ejecución de microservicios

1. Abrir XAMPP.
2. Iniciar el servicio **MySQL**.
3. Ejecutar cada microservicio desde su carpeta:

```bash
mvn spring-boot:run
```

4. Orden recomendado de ejecución:

```text
user-service
auth-service
game-service
tournament-service
team-service
sanction-service
registration-service
match-service
result-service
ranking-service
prize-service
notification-service
```

5. Probar los flujos principales con la colección


---

## Endpoints principales por microservicio

### auth-service (8080)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/auth/registro` | Crear cuenta |
| POST | `/api/v1/auth/login` | Login → JWT |
| POST | `/api/v1/auth/validar-token` | Validar JWT (para Gateway) |
| GET  | `/api/v1/auth/cuentas` | Listar cuentas |
| PUT  | `/api/v1/auth/cuentas/{id}` | Actualizar rol/estado/password |
| DELETE | `/api/v1/auth/cuentas/{id}/desactivar` | Desactivar cuenta |

### user-service (8081)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/usuarios` | Crear usuario |
| GET  | `/api/v1/usuarios?rol=JUGADOR` | Listar por rol |
| GET  | `/api/v1/usuarios/{id}` | Buscar por ID |
| PUT  | `/api/v1/usuarios/{id}` | Actualizar |
| DELETE | `/api/v1/usuarios/{id}/desactivar` | Desactivar |

### team-service (8082)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/equipos` | Crear equipo (capitán auto-añadido) |
| GET  | `/api/v1/equipos?estado=ACTIVO` | Listar |
| GET  | `/api/v1/equipos/{id}` | Buscar con miembros |
| POST | `/api/v1/equipos/{id}/miembros` | Agregar miembro |
| DELETE | `/api/v1/equipos/{id}/desactivar` | Desactivar |

### tournament-service (8083)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/torneos` | Crear torneo |
| GET  | `/api/v1/torneos?estado=ABIERTO` | Listar por estado |
| PATCH | `/api/v1/torneos/{id}/estado?nuevoEstado=EN_CURSO` | Cambiar estado |
| DELETE | `/api/v1/torneos/{id}/cancelar` | Cancelar |

### registration-service (8084)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/inscripciones` | Inscribir (valida todo) |
| GET  | `/api/v1/inscripciones/torneo/{torneoId}` | Por torneo |
| PATCH | `/api/v1/inscripciones/{id}/estado` | Cambiar estado |
| DELETE | `/api/v1/inscripciones/{id}/cancelar` | Cancelar |

### match-service (8086)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/partidas` | Crear partida |
| GET  | `/api/v1/partidas?torneoId=1&estado=PROGRAMADA` | Listar con filtros |
| PATCH | `/api/v1/partidas/{id}` | Actualizar estado/horario |
| DELETE | `/api/v1/partidas/{id}/cancelar` | Cancelar |

### game-service (8087)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/v1/juegos` | Crear juego |
| GET  | `/api/v1/juegos` | Listar activos |
| GET  | `/api/v1/juegos?todos=true` | Listar todos |
| DELETE | `/api/v1/juegos/{id}/desactivar` | Desactivar |

---

## Ejemplo de flujo completo con curl

```bash
# 1. Crear juego
curl -X POST http://localhost:8087/api/v1/juegos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Valorant","genero":"FPS","modalidad":"EQUIPO","jugadoresPorEquipo":5}'

# 2. Crear torneo
curl -X POST http://localhost:8083/api/v1/torneos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Copa Verano 2026","juegoId":1,"fechaInicio":"2026-06-01",
       "fechaFin":"2026-06-30","fechaFinInscripcion":"2026-05-28",
       "cupoMaximo":16,"modalidad":"ELIMINACION_DIRECTA"}'

# 3. Abrir torneo
curl -X PATCH "http://localhost:8083/api/v1/torneos/1/estado?nuevoEstado=ABIERTO"

# 4. Crear equipo
curl -X POST http://localhost:8082/api/v1/equipos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Team Alpha","capitanId":1,"juegoPrincipalId":1}'

# 5. Inscribir equipo
curl -X POST http://localhost:8084/api/v1/inscripciones \
  -H "Content-Type: application/json" \
  -d '{"torneoId":1,"equipoId":1,"tipoParticipante":"EQUIPO"}'
```

---


---

## Documentación Swagger

Agregar en cada `pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

Luego acceder a: `http://localhost:<puerto>/swagger-ui/index.html`

---

## Evidencias requeridas

- [x] Repositorio GitHub organizado por microservicios
- [x] `README.md` con puertos y endpoints
- [x] Colección Postman exportada 
- [x] Diagrama de ecosistema 
- [ ] Tablero Trello con tareas distribuidas
- [ ] Swagger/OpenAPI 
- [ ] API Gateway configurado 

