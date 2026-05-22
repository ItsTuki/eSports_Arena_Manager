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
| `auth-service`       | 8080   | `db_auth`          | 3310        |
| `user-service`       | 8081   | `db_users`         | 3311        |
| `team-service`       | 8082   | `db_teams`         | 3312        |
| `tournament-service` | 8083   | `db_tournaments`   | 3314        |
| `registration-service` | 8084 | `db_registration`  | 3315        |
| `sanction-service`   | 8085   | `db_sanctions`     | 3317        |
| `match-service`      | 8086   | `db_matches`       | 3316        |
| `game-service`       | 8087   | `db_games`         | 3313        |
| `result-service`     | 8088   | `db_results`       | 3318        |
| `ranking-service`    | 8089   | `db_rankings`      | 3319        |
| `prize-service`      | 8090   | `db_prizes`        | 3320        |
| `notification-service` | 8091 | `db_notifications` | 3321        |

---
## Diagramas del Sistema

### 1. Ecosistema de Microservicios
Este diagrama representa la topología de la arquitectura distribuida del proyecto. Muestra cómo las peticiones de los clientes ingresan de manera centralizada a través del **API Gateway**, la interacción con el servidor de descubrimiento **Eureka**, y el flujo de comunicación sincrónica entre los servicios core del sistema.

![Diagrama del Ecosistema de Microservicios](https://imgur.com/a/HgFIGOK)

### 2. Modelo Relacional de Base de Datos
Fiel al principio del diseño de microservicios, cada servicio cuenta con su propia persistencia e independencia de datos (esquemas aislados). Las referencias cruzadas entre entidades de diferentes servicios se acoplan lógicamente mediante código de negocio en la capa Service, garantizando el desacoplamiento físico de las bases de datos.

![Modelo Relacional de Base de Datos](https://imgur.com/a/2v3TfW5)
## Grafo de comunicación entre servicios

```
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
```

---

## Flujo integrador principal

```
1. Admin registra juego (game-service)
2. Admin crea torneo asociado al juego (tournament-service)
3. Jugadores crean equipos (team-service)
4. registration-service valida cupo + sanciones + estado torneo → inscribe
5. match-service genera partidas entre inscritos
6. result-service registra y valida resultados
7. ranking-service recalcula posiciones
8. prize-service asigna premios → notification-service notifica
```

---

## Instrucciones de ejecución

#

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
- [ ] Colección Postman exportada 
- [ ] Diagrama de ecosistema 
- [ ] Tablero Trello con tareas distribuidas
- [ ] Swagger/OpenAPI 
- [ ] API Gateway configurado 

