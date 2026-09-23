package com.banco.account.messaging;

import com.banco.account.domain.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClientEventConsumer {
    private final ClientProjectionRepository clients;

    public ClientEventConsumer(ClientProjectionRepository c) {
        clients = c;
    }

    @RabbitListener(queues = RabbitConfiguration.QUEUE)
    @Transactional
    public void consume(ClientChangedEvent e) {
        if ("DELETED".equals(e.operation())) {
            clients.deleteById(e.clientId());
            return;
        }
        var c = clients.findById(e.clientId()).orElseGet(ClientProjection::new);
        if (c.getUpdatedAt() != null && c.getUpdatedAt().isAfter(e.occurredAt()))
            return;
        c.setClientId(e.clientId());
        c.setName(e.name());
        c.setActive(e.active());
        c.setUpdatedAt(e.occurredAt());
        clients.save(c);
    }
}
