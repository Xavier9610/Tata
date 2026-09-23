package com.banco.client.messaging;

import com.banco.client.domain.Client;
import java.time.Instant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientEventPublisher {
    private final RabbitTemplate rabbit;
    private final boolean enabled;

    public ClientEventPublisher(RabbitTemplate rabbit, @Value("${bank.messaging.enabled:false}") boolean enabled) {
        this.rabbit = rabbit;
        this.enabled = enabled;
    }

    public void publish(Client c, String operation) {
        if (enabled)
            rabbit.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.KEY,
                    new ClientChangedEvent(c.getClientId(), c.getName(), c.isActive(), operation, Instant.now()));
    }
}
