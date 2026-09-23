CREATE DATABASE banco_clientes;
CREATE DATABASE banco_cuentas;
\connect banco_clientes
CREATE TABLE personas (id BIGSERIAL PRIMARY KEY, nombre VARCHAR(120) NOT NULL, genero VARCHAR(30), edad INTEGER, identificacion VARCHAR(40) UNIQUE NOT NULL, direccion VARCHAR(200), telefono VARCHAR(30));
CREATE TABLE clientes (id BIGINT PRIMARY KEY REFERENCES personas(id) ON DELETE CASCADE, cliente_id VARCHAR(40) UNIQUE NOT NULL, password_hash VARCHAR(100) NOT NULL, activo BOOLEAN NOT NULL DEFAULT TRUE, version BIGINT NOT NULL DEFAULT 0);
\connect banco_cuentas
CREATE TABLE cliente_projection (cliente_id VARCHAR(40) PRIMARY KEY, nombre VARCHAR(120) NOT NULL, activo BOOLEAN NOT NULL, updated_at TIMESTAMPTZ NOT NULL);
CREATE TABLE cuentas (numero_cuenta VARCHAR(40) PRIMARY KEY, tipo VARCHAR(20) NOT NULL, saldo_inicial NUMERIC(19,2) NOT NULL CHECK (saldo_inicial >= 0), saldo_actual NUMERIC(19,2) NOT NULL CHECK (saldo_actual >= 0), activa BOOLEAN NOT NULL DEFAULT TRUE, cliente_id VARCHAR(40) NOT NULL, version BIGINT NOT NULL DEFAULT 0);
CREATE TABLE movimientos (id BIGSERIAL PRIMARY KEY, numero_cuenta VARCHAR(40) NOT NULL REFERENCES cuentas(numero_cuenta), fecha TIMESTAMPTZ NOT NULL, tipo VARCHAR(20) NOT NULL, valor NUMERIC(19,2) NOT NULL CHECK (valor <> 0), estado BOOLEAN NOT NULL DEFAULT TRUE, saldo NUMERIC(19,2) NOT NULL CHECK (saldo >= 0), idempotency_key VARCHAR(100) UNIQUE);
CREATE INDEX idx_movimientos_cuenta_fecha ON movimientos(numero_cuenta, fecha);
