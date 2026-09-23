package com.banco.account.service;

import com.banco.account.api.*;
import com.banco.account.domain.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovementService {
    private final AccountRepository accounts;
    private final MovementRepository movements;

    public MovementService(AccountRepository a, MovementRepository m) {
        accounts = a;
        movements = m;
    }

    @Transactional
    public MovementResponse create(MovementRequest r, String key) {
        if (key != null && !key.isBlank()) {
            var repeated = movements.findByIdempotencyKey(key);
            if (repeated.isPresent())
                return dto(repeated.get());
        }
        var a = lock(r.numeroCuenta());
        ensureActive(a);
        var m = new Movement();
        m.setAccountNumber(a.getNumber());
        m.setOccurredAt(r.fecha() == null ? Instant.now() : r.fecha());
        m.setType(r.tipoMovimiento());
        m.setAmount(signed(r));
        m.setActive(r.estado());
        var estimated = a.getCurrentBalance().add(m.isActive() ? m.getAmount() : BigDecimal.ZERO);
        if (estimated.signum() < 0)
            throw new ApiException(422, "Saldo no disponible");
        m.setBalanceAfter(estimated);
        m.setIdempotencyKey(key == null || key.isBlank() ? null : key);
        movements.save(m);
        rebuild(a);
        return dto(m);
    }

    @Transactional
    public MovementResponse update(Long id, MovementRequest r) {
        var m = get(id);
        if (!m.getAccountNumber().equals(r.numeroCuenta()))
            throw new ApiException(400, "No se puede mover una transacción a otra cuenta");
        var a = lock(r.numeroCuenta());
        ensureActive(a);
        m.setOccurredAt(r.fecha() == null ? m.getOccurredAt() : r.fecha());
        m.setType(r.tipoMovimiento());
        m.setAmount(signed(r));
        m.setActive(r.estado());
        rebuild(a);
        return dto(m);
    }

    @Transactional
    public void delete(Long id) {
        var m = get(id);
        var a = lock(m.getAccountNumber());
        movements.delete(m);
        movements.flush();
        rebuild(a);
    }

    public List<MovementResponse> list(String accountNumber, java.time.LocalDate fechaInicio,
            java.time.LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio))
            throw new ApiException(400, "La fecha final debe ser posterior o igual a la inicial");
        var from = fechaInicio == null ? null : fechaInicio.atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        var to = fechaFin == null ? null : fechaFin.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        return movements.findAllByOrderByOccurredAtAscIdAsc().stream()
                .filter(m -> accountNumber == null || accountNumber.isBlank()
                        || m.getAccountNumber().equals(accountNumber.trim()))
                .filter(m -> from == null || !m.getOccurredAt().isBefore(from))
                .filter(m -> to == null || m.getOccurredAt().isBefore(to)).map(this::dto).toList();
    }

    private Account lock(String n) {
        return accounts.lockByNumber(n).orElseThrow(() -> new ApiException(404, "Cuenta no encontrada"));
    }

    private Movement get(Long id) {
        return movements.findById(id).orElseThrow(() -> new ApiException(404, "Movimiento no encontrado"));
    }

    private void ensureActive(Account a) {
        if (!a.isActive())
            throw new ApiException(422, "La cuenta está inactiva");
    }

    private BigDecimal signed(MovementRequest r) {
        return r.tipoMovimiento() == MovementType.RETIRO ? r.valor().negate() : r.valor();
    }

    private void rebuild(Account a) {
        BigDecimal balance = a.getOpeningBalance();
        for (var item : movements.findByAccountNumberOrderByOccurredAtAscIdAsc(a.getNumber())) {
            if (item.isActive())
                balance = balance.add(item.getAmount());
            if (balance.signum() < 0)
                throw new ApiException(422, "Saldo no disponible");
            item.setBalanceAfter(balance);
        }
        a.setCurrentBalance(balance);
    }

    private MovementResponse dto(Movement m) {
        return new MovementResponse(m.getId(), m.getAccountNumber(), m.getOccurredAt(), m.getType(), m.getAmount(),
                m.isActive(), m.getBalanceAfter());
    }
}
