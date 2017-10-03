package com.ms.notificationservice.configuration.integration;

import com.ms.notifiactionervice.integration.xsd.OrderReady;
import com.ms.notificationservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.channel.MessageChannels;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.concurrent.Executors;

public class OrderReadyIncommingFlowConfiguration extends AbstractNotificationFlowConfiguration {

    @Value("${orderReadyEvent.in.amqp.queue}")
    private String queueOrderReady;

    @Autowired
    private MailService mailService;

    @Bean
    public IntegrationFlow amqpNewOrderEventInboundFlow() {
        return IntegrationFlows.from(Amqp.inboundAdapter(rabbitMQConnection(), queueOrderReady))
                .channel(MessageChannels.executor(Executors.newCachedThreadPool()))
                .transform(this::performOrderReadyUnmarshaling)
                .handle(m -> sentNotifiation((OrderReady) m.getPayload()))
                .get();
    }

    private void sentNotifiation(OrderReady orderReady) {
        mailService.sendMessageOrderIsReady(orderReady.getEMail());
    }

    @Bean
    public JAXBContext jaxbContextOrderReadyEvent() throws JAXBException {
        return JAXBContext.newInstance(OrderReady.class);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Unmarshaller orderReadyEventUnmarshaler() throws JAXBException {
        return jaxbContextOrderReadyEvent().createUnmarshaller();
    }


    protected OrderReady performOrderReadyUnmarshaling(String xml) {
        OrderReady orderReady = null;
        try {
            orderReady = (OrderReady) orderReadyEventUnmarshaler().unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return orderReady;
    }
}
