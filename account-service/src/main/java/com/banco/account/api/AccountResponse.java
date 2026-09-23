package com.banco.account.api;

import com.banco.account.domain.AccountType;
import java.math.BigDecimal;

public record AccountResponse(String numeroCuenta, AccountType tipo, BigDecimal saldoInicial, boolean estado,
        String cliente) {
}
