package com.banco.account.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "cliente_projection")
public class ClientProjection {
    @Id
    @Column(name = "cliente_id")
    private String clientId;
    @Column(name = "nombre", nullable = false)
    private String name;
    @Column(name = "activo", nullable = false)
    private boolean active;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String v) {
        clientId = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean v) {
        active = v;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant v) {
        updatedAt = v;
    }
}
