package com.banco.account.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
public class Account {
    @Id
    @Column(name = "numero_cuenta", length = 40)
    private String number;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private AccountType type;
    @Column(name = "saldo_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal openingBalance;
    @Column(name = "saldo_actual", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentBalance;
    @Column(name = "activa", nullable = false)
    private boolean active = true;
    @Column(name = "cliente_id", nullable = false)
    private String clientId;
    @Version
    private long version;
    public String getNumber() { return number; }
    public void setNumber(String value) { number = value; }
    public AccountType getType() { return type; }
    public void setType(AccountType value) { type = value; }
    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal value) { openingBalance = value; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal value) { currentBalance = value; }
    public boolean isActive() { return active; }
    public void setActive(boolean value) { active = value; }
    public String getClientId() { return clientId; }
    public void setClientId(String value) { clientId = value; }
}
