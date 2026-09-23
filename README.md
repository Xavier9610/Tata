# Plataforma bancaria - prueba técnica Senior

Dos servicios Spring Boot: `Servicio 1` (personas y clientes, puerto 8081) y `Servicio 2` (cuentas, movimientos, reporte y UI, puerto 8082). Los cambios de clientes se publican como eventos RabbitMQ y el segundo servicio mantiene una proyección local para validar titulares y generar reportes sin llamadas síncronas.
Ademas se implementa una base de datos relacional PostgresSql.

## Arranque rápido

Con Docker Desktop iniciado, abre PowerShell dentro de esta carpeta y ejecuta:

docker compose up --build

Abre http://localhost:8082 para la interfaz. 

<img width="1320" height="972" alt="image" src="https://github.com/user-attachments/assets/8d4fa0a4-046f-4a93-a9a2-addac773884c" />


Swagger de clientes: http://localhost:8081/swagger-ui/index.html; 

<img width="1465" height="567" alt="image" src="https://github.com/user-attachments/assets/2d13bb4c-7522-4ed6-9fb9-b79cba307a58" />

Swagger de cuentas y movimientos: http://localhost:8082/swagger-ui/index.html. 

<img width="1230" height="797" alt="image" src="https://github.com/user-attachments/assets/b69b5b21-7469-43c6-9d04-bd93cff326fa" />

RabbitMQ queda en http://localhost:15672 (guest/guest). 

<img width="1182" height="710" alt="image" src="https://github.com/user-attachments/assets/78aa92a6-a655-4d5d-8a62-bc1aaf826ab4" />

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
