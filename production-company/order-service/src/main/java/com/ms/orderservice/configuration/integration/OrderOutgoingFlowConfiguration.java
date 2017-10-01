package com.ms.orderservice.configuration.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.Executors;

@Configuration
public class OrderOutgoingFlowConfiguration extends AbstractOrderFlowConfiguration {

    @Value("${newOrderEvent.out.amqp.exchange}")
    private String newOrderEventOutAmqpExchange;

    @Value("${newOrderEvent.out.amqp.routingKey}")
    private String newOrderEventOutAmqpRoutingKey;

   @Bean(name= IntegrationFlowKeys.NEW_ORDER_OUT_CHANNEL)
    public MessageChannel newOrderOutChannel(){
        return MessageChannels.executor(Executors.newCachedThreadPool()).get();
   }

    @Bean
    public IntegrationFlow amqpOutboundFlow() {
        return IntegrationFlows.from(newOrderOutChannel())
                .transform(this::performMarshaling)
                .handle(Amqp.outboundAdapter(amqpTemplate())
                        .exchangeName(newOrderEventOutAmqpExchange)
                        .routingKey(newOrderEventOutAmqpRoutingKey))
                .get();
    }

}
