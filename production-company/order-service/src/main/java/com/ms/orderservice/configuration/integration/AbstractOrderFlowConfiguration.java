package com.ms.orderservice.configuration.integration;


import com.ms.orderservice.integration.xsd.NewOrderEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;


@Configuration
public class AbstractOrderFlowConfiguration {

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
    public AmqpTemplate amqpTemplate() {
        AmqpTemplate amqpTemplate = new RabbitTemplate(rabbitMQConnection());
        return amqpTemplate;
    }

    @Bean
    public JAXBContext jaxbContextNewOrderEvent() throws JAXBException {
        return JAXBContext.newInstance(NewOrderEvent.class);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Marshaller newOrderEventMarshaler() throws JAXBException {
        return jaxbContextNewOrderEvent().createMarshaller();
    }

    protected String performMarshaling(NewOrderEvent newOrderEvent) {
        StringWriter writer = new StringWriter();
        try {
            newOrderEventMarshaler().marshal(newOrderEvent,writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
