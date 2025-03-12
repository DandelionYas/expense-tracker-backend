package com.expense.common.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(rabbitMqProperties.getQueueName()).build();
    }

    @Bean
    public Queue jsonQueue() {
        return QueueBuilder.durable(rabbitMqProperties.getJsonQueueName()).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(rabbitMqProperties.getExchangeName());
    }

    @Bean
    public Binding jsonBinding(@Qualifier("jsonQueue") Queue queue,
                               @Qualifier("exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitMqProperties.getJsonRoutingKey());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(@Qualifier("messageConverter") MessageConverter messageConverter,
                                     ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
