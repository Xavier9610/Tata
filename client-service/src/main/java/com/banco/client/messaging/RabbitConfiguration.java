package com.banco.client.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfiguration {
    public static final String EXCHANGE = "bank.client.events", QUEUE = "account.client.events",
            DLQ = "account.client.events.dlq", KEY = "client.changed";

    @Bean
    TopicExchange clientExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue clientDeadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    Queue clientQueue() {
        return QueueBuilder.durable(QUEUE).deadLetterExchange(EXCHANGE + ".dlx").deadLetterRoutingKey(KEY).build();
    }

    @Bean
    DirectExchange dlx() {
        return new DirectExchange(EXCHANGE + ".dlx");
    }

    @Bean
    Binding bind() {
        return BindingBuilder.bind(clientQueue()).to(clientExchange()).with(KEY);
    }

    @Bean
    Binding bindDlq() {
        return BindingBuilder.bind(clientDeadLetterQueue()).to(dlx()).with(KEY);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
