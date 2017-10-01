package com.ms.orderservice.configuration.integration;


import com.ms.orderservice.integration.xsd.Event;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;


@Configuration
public class AbstractOrderRealizationFlowConfiguration {

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

    @Bean
    public Unmarshaller eventUnmarshaler() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Event.class);
        return jaxbContext.createUnmarshaller();
    }

    protected Event performUnmarshaling(String xml) {
        Event event=null;
        try {
             event = (Event) eventUnmarshaler().unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return event;
    }
}
