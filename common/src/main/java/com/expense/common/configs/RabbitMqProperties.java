package com.expense.common.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {
    private String jsonQueueName="default";
    private String exchangeName="default";
    private String jsonRoutingKey="default";
}
