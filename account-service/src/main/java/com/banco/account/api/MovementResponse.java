package com.banco.account.api;

import com.banco.account.domain.MovementType;
import java.math.BigDecimal;
import java.time.Instant;

public record MovementResponse(Long id, String numeroCuenta, Instant fecha, MovementType tipoMovimiento,
        BigDecimal valor, boolean estado, BigDecimal saldo) {
}
