# Plataforma bancaria - prueba técnica Senior

Dos servicios Spring Boot: `Servicio 1` (personas y clientes, puerto 8081) y `Servicio 2` (cuentas, movimientos, reporte y UI, puerto 8082). Los cambios de clientes se publican como eventos RabbitMQ y el segundo servicio mantiene una proyección local para validar titulares y generar reportes sin llamadas síncronas.

## Arranque rápido

Con Docker Desktop iniciado, abre PowerShell dentro de esta carpeta y ejecuta:

docker compose up --build

Abre http://localhost:8082 para la interfaz. 


Swagger de clientes: http://localhost:8081/swagger-ui/index.html; 


Swagger de cuentas y movimientos: http://localhost:8082/swagger-ui/index.html. 


RabbitMQ queda en http://localhost:15672 (guest/guest). 


Para detener todo: `docker compose down`.

## Endpoints

- `GET|POST /clientes`, `GET|PUT|DELETE /clientes/{id}` en el puerto 8081. El listado muestra el modelo solicitado
- `GET|POST /cuentas`, `GET|PUT|DELETE /cuentas/{number}` en el puerto 8082. La cuenta se crea con la identificación del cliente y se lista con su nombre.
- `GET|POST /movimientos?numeroCuenta={numero}`, `PUT|DELETE /movimientos/{id}`. Para evitar duplicados, envíe un encabezado `Idempotency-Key` en POST.
- `GET /reportes?identificacion={identificacion}&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD`. Los tres filtros son opcionales.

## Decisiones relevantes

- JPA/Hibernate, DTOs, Bean Validation, RFC 7807 Problem Details y contraseñas BCrypt.
- Saldo actual y versión optimista en cuenta; las operaciones de saldo se serializan con bloqueo pesimista para evitar sobregiros concurrentes.
- Las operaciones de movimiento llevan `Idempotency-Key` opcional; las repeticiones devuelven el mismo movimiento.
- `BaseDatos.sql`, `docker-compose.yml`, Dockerfiles, colección Postman, pruebas unitarias e integración están incluidos.
- RabbitMQ incluye DLQ. En una instalación productiva se recomienda sustituir el publicador directo por patrón outbox y monitorizar la cola de errores.
