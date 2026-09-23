package com.banco.account.api;

import com.banco.account.domain.AccountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AccountRequest(@NotBlank @Size(max = 40) String numeroCuenta, @NotNull AccountType tipo,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 17, fraction = 2) BigDecimal saldoInicial,
        @NotNull Boolean estado, @NotBlank @Size(max = 40) String clienteIdentificacion) {
}
