package com.banco.account.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "movimientos")
public class Movement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "numero_cuenta", nullable = false) private String accountNumber;
    @Column(name = "fecha", nullable = false) private Instant occurredAt;
    @Enumerated(EnumType.STRING) @Column(name = "tipo", nullable = false) private MovementType type;
    @Column(name = "valor", nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Column(name = "estado", nullable = false) private boolean active = true;
    @Column(name = "saldo", nullable = false, precision = 19, scale = 2) private BigDecimal balanceAfter;
    @Column(name = "idempotency_key", unique = true, length = 100) private String idempotencyKey;
    public Long getId() { return id; } public String getAccountNumber() { return accountNumber; } public void setAccountNumber(String value) { accountNumber = value; }
    public Instant getOccurredAt() { return occurredAt; } public void setOccurredAt(Instant value) { occurredAt = value; }
    public MovementType getType() { return type; } public void setType(MovementType value) { type = value; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal value) { amount = value; }
    public boolean isActive() { return active; } public void setActive(boolean value) { active = value; }
    public BigDecimal getBalanceAfter() { return balanceAfter; } public void setBalanceAfter(BigDecimal value) { balanceAfter = value; }
    public String getIdempotencyKey() { return idempotencyKey; } public void setIdempotencyKey(String value) { idempotencyKey = value; }
}
