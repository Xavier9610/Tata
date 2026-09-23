package com.banco.account.messaging;

import java.time.Instant;

public record ClientChangedEvent(String clientId, String name, boolean active, String operation, Instant occurredAt) {
}
