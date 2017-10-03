package com.ms.warehouseservice.configuration.integration;

import com.ms.warehouseservice.integration.xsd.OrderReady;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.concurrent.Executors;

@Configuration
public class OrderReadyOutgoingFlowConfiguration extends AbstractWarehouseFlowConfiguration{

    @Value("${orderReady.out.amqp.exchange}")
    private String readyOrdersEventOutAmqpExchange;

    @Value("${orderReady.out.amqp.routingKey}")
    private String readyOrdersEventOutAmqpRoutingKey;

    @Bean(name= IntegrationFlowKeys.NEW_ORDER_OUT_CHANNEL)
    public MessageChannel newOrderOutChannel(){
        return MessageChannels.executor(Executors.newCachedThreadPool()).get();
    }

    @Bean
    public IntegrationFlow amqpOutboundFlow() {
        return IntegrationFlows.from(newOrderOutChannel())
                .transform(this::performMarshalingOrderReady)
                .handle(Amqp.outboundAdapter(amqpTemplate())
                        .exchangeName(readyOrdersEventOutAmqpExchange)
                        .routingKey(readyOrdersEventOutAmqpRoutingKey))
                .get();
    }

    @Bean
    public JAXBContext jaxbContextOrderReadyEvent() throws JAXBException {
        return JAXBContext.newInstance(OrderReady.class);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Marshaller orderReadyEventMarshaler() throws JAXBException {
        return jaxbContextOrderReadyEvent().createMarshaller();
    }


    protected String performMarshalingOrderReady(OrderReady orderReady) {
        StringWriter writer = new StringWriter();
        try {
            orderReadyEventMarshaler().marshal(orderReady,writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

}
