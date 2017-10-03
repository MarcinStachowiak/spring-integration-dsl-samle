package com.ms.notificationservice.configuration.integration;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AbstractNotificationFlowConfiguration {

    @Bean
    public CachingConnectionFactory rabbitMQConnection(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public AmqpTemplate amqpTemplate(){
        AmqpTemplate amqpTemplate = new RabbitTemplate(rabbitMQConnection());
        return amqpTemplate;
    }
}
