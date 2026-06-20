# eSports Arena Manager

Asignatura: Desarrollo FullStack I DSY1103  
Institución:DuocUC  
Arquitectura: Microservicios con Spring Boot 3.2.5 + Java 21

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

También queda disponible el JSON OpenAPI en `/v3/api-docs` dentro de cada microservicio.

## API Gateway, Eureka y HATEOAS

El proyecto incorpora un servidor Eureka para descubrimiento de servicios y un API Gateway como punto unico de entrada.

| Componente | URL |
|------------|-----|
| Eureka Dashboard | `http://localhost:8761` |
| API Gateway | `http://localhost:8070` |

Las rutas publicas del Gateway mantienen los mismos paths `/api/v1` de cada microservicio. Ejemplos:

```text
http://localhost:8070/api/v1/usuarios
http://localhost:8070/api/v1/equipos
http://localhost:8070/api/v1/torneos
http://localhost:8070/api/v1/inscripciones
```

Cada microservicio tambien agrega enlaces HATEOAS en la cabecera HTTP `Link` para las rutas `/api/v1/**`, sin cambiar el JSON de respuesta. Esto permite mantener compatibilidad con Feign y Postman.

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

Los `application.properties` usan `createDatabaseIfNotExist=true`, por lo que Hibernate puede crear las bases si el usuario `root` tiene permisos.

### Ejecución de microservicios

1. Abrir XAMPP.
2. Iniciar el servicio **MySQL**.
3. Ejecutar primero el servidor Eureka:

```bash
cd discovery-server
mvn spring-boot:run
```

4. Ejecutar cada microservicio desde su carpeta:

```bash
mvn spring-boot:run
```

5. Orden recomendado de ejecución:

```text
discovery-server
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
api-gateway
```

6. Ejecutar el Gateway al final:

```bash
cd api-gateway
mvn spring-boot:run
```

7. Probar los flujos principales con la colección de postman(collecion de postman.txt), usando `http://localhost:8070` si se quiere probar por Gateway.



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

## Evidencias requeridas

- [x] Repositorio GitHub organizado por microservicios
- [x] `README.md` con puertos y endpoints
- [x] Colección Postman exportada 
- [x] Diagrama de ecosistema 
- [ ] Tablero Trello con tareas distribuidas

