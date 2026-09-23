package com.banco.account.api;

import com.banco.account.domain.AccountType;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public record ReportResponse(String identificacion, String clientName, LocalDate fechaInicio, LocalDate fechaFin,
        List<AccountReport> accounts) {
    public record AccountReport(String number, AccountType type, BigDecimal openingBalance, BigDecimal currentBalance,
            boolean active, List<MovementItem> movements) {
    }

    public record MovementItem(Long id, Instant occurredAt, String type, BigDecimal amount, BigDecimal balanceAfter) {
    }
}
