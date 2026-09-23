package com.banco.account.api;

import com.banco.account.domain.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record MovementRequest(@NotBlank String numeroCuenta, @NotNull MovementType tipoMovimiento,
        @NotNull @DecimalMin(value = "0.01") @Digits(integer = 17, fraction = 2) BigDecimal valor,
        @NotNull Boolean estado, Instant fecha) {
}
