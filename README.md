# GeoMed — Proyecto Integrador de Arquitectura Empresarial (2026-01)

Este repositorio contiene el sistema integrado **GeoMed**, compuesto por el API Gateway (NGINX), el microservicio de negocio `geomed-service` y el microservicio de gestión `users-service`. Ambas aplicaciones están desarrolladas en Kotlin con Spring Boot, JPA, Gradle y siguen una arquitectura en capas limpia, desacoplada y orientada a microservicios.

## Integrantes yNRC
- **NRC:** 1462
- **Proyecto:** GeoMed - Gestión de Pacientes, Citas Médicas y Disponibilidad de Médicos

---

## Estándar de Logging (Criterio 2)

Ambos microservicios (`geomed-service` y `users-service`) implementan un estándar de logging consistente, estructurado y optimizado para una sola línea (en formato amigable para centralizadores de logs como CloudWatch, Loki, etc.), evitando formatos multilinea complejos (salvo excepciones como stacktraces).

### Formato Obligatorio
Cada log console tiene un formato de una sola línea con campos separados por el delimitador ` | ` en el siguiente orden fijo:
```
<timestamp> | <LEVEL> | <servicio> | sub=<cognito-sub|anonimo> | <logger> | event=<evento> | msg=<mensaje> | <clave=valor ...>
```

#### Ejemplos Reales de Logs Generados por el Sistema:
```text
2026-08-02T14:23:11.301Z | INFO  | geomed-service | sub=a1b2c3d4-e5f6-7890-abcd-ef1234567890 | c.p.g.controllers.AppointmentController | event=http.request | msg=POST /api/appointments
2026-08-02T14:23:11.482Z | INFO  | geomed-service | sub=a1b2c3d4-e5f6-7890-abcd-ef1234567890 | c.p.g.services.AppointmentService       | event=appointment.created | msg=Cita creada exitosamente | appointmentId=17 patientId=1 doctorId=2
2026-08-02T14:23:11.495Z | INFO  | geomed-service | sub=a1b2c3d4-e5f6-7890-abcd-ef1234567890 | c.p.g.controllers.AppointmentController | event=http.response | msg=201 POST /api/appointments
```

### Configuración del Formato en Logback / Spring Boot
Ambos servicios configuran el formato de consola de la siguiente manera:
```yaml
logging:
  pattern:
    console: "%d{yyyy-MM-dd'T'HH:mm:ss.SSSXXX} | %-5level | ${spring.application.name} | sub=%X{sub:-anonimo} | %logger{40} | %msg%n"
```
El `sub` se recupera automáticamente del token JWT de AWS Cognito mediante un filtro `MdcLoggingFilter` y se añade al `MDC`.

### Logging en Bases de Datos
Ambos microservicios tienen configurado el logging detallado de consultas SQL en JPA/Hibernate y también a nivel de motor PostgreSQL:
#### 1. Nivel Aplicación (Spring Boot):
```yaml
spring:
  jpa:
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        generate_statistics: true
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.orm.jdbc.bind: TRACE
```
#### 2. Nivel Motor (docker-compose.yml):
Ambos contenedores de Postgres (`users-db` y `geomed-db`) ejecutan comandos dedicados para loguear cada sentencia:
```yaml
command: >
  postgres
  -c log_statement=all
  -c log_duration=on
  -c log_line_prefix='%m [%p] %u@%d app=%a '
```

---

## Arquitectura y Componentes (Criterio 1)

El sistema utiliza un API Gateway centralizado mediante NGINX que expone de manera segura los microservicios sin exponer sus puertos directamente al exterior.

```
                  ┌────────────────────────┐
                  │     NGINX (Port 80)    │
                  └───────────┬────────────┘
                              │
             ┌────────────────┴────────────────┐
             ▼                                 ▼
┌────────────────────────┐         ┌────────────────────────┐
│ users-service (8787)   │         │ geomed-service (8788)  │
└────────────┬───────────┘         └────────────┬───────────┘
             ▼                                  ▼
┌────────────────────────┐         ┌────────────────────────┐
│  users-db (Postgres)   │         │  geomed-db (Postgres)  │
└────────────────────────┘         └────────────────────────┘
```

- **Database-per-Service:** `users-service` y `geomed-service` poseen sus propias bases de datos aisladas en contenedores separados de PostgreSQL 16 con credenciales, volúmenes y esquemas independientes.
- **Estrategia de Soft Delete (Criterio 6):** Todas las entidades heredan de `BaseEntity`, la cual provee la propiedad `deletedAt`. Todas las consultas del sistema utilizan filtros de tipo `AndDeletedAtIsNull` en repositorios y servicios para asegurar que los elementos eliminados lógicamente no aparezcan en listados de negocio ni consultas de detalle.
- ** AWS Cognito (Criterio 8):** Ambos servicios están protegidos como servidores de recursos OAuth2 y validan tokens JWT generados por el mismo Issuer de AWS Cognito, mapeando los grupos del Cognito a roles de Spring Security (`ROLE_ADMIN`, `ROLE_USER`).

---

## Cómo Levantar el Proyecto (Criterio 1)

1. Duplica el archivo `.env.example` como `.env` e introduce los valores apropiados de tu pool de AWS Cognito.
2. Asegúrate de tener instalado Docker y Docker Compose.
3. Levanta todos los servicios ejecutando:
   ```bash
   docker compose up -d --build
   ```
4. Los endpoints están expuestos **únicamente** a través del puerto `80` expuesto por el proxy de NGINX:
   - Usuarios: `http://localhost/users/` (redirige internamente a `users-service`)
   - GeoMed: `http://localhost/geomed/` (redirige internamente a `geomed-service`)

---

## Tests y Cobertura (Criterio 6)

Ambos microservicios cuentan con cobertura de pruebas unitarias y de integración que alcanzan el **100% de la lógica propia de negocio** (excluyendo DTOs, entidades de persistencia puras y clases generadas o de configuración técnica de frameworks).

Para ejecutar las pruebas en cada microservicio, ejecuta:
```bash
# Para geomed-service
./geomed-service/gradlew -p geomed-service test

# Para users-service
./users-service/gradlew -p users-service test
```

---

## Colección de Postman (Criterio 7)

La colección de Postman configurada para interactuar con NGINX y realizar las pruebas del flujo completo se encuentra en la raíz del repositorio con el nombre:
- `GeoMed_Collection.json`

Permite realizar peticiones dinámicas utilizando variables de entorno de Postman (como `{{baseUrl}}`, `{{patientId}}`, etc.).
