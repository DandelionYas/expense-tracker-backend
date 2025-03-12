package com.expense.publishers;

import com.expense.common.configs.RabbitMqProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public void send(Object object) {
        rabbitTemplate.convertAndSend(rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getJsonRoutingKey(),
                object);
    }
}
