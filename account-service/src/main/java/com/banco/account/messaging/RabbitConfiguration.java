package com.banco.account.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfiguration {
    public static final String EXCHANGE = "bank.client.events", QUEUE = "account.client.events",
            DLQ = "account.client.events.dlq", KEY = "client.changed";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue dead() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(QUEUE).deadLetterExchange(EXCHANGE + ".dlx").deadLetterRoutingKey(KEY).build();
    }

    @Bean
    DirectExchange dlx() {
        return new DirectExchange(EXCHANGE + ".dlx");
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(KEY);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(dead()).to(dlx()).with(KEY);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
