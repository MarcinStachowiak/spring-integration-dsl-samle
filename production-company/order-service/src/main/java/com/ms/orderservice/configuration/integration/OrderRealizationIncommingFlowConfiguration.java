package com.ms.orderservice.configuration.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;

import java.util.concurrent.Executors;

@Configuration
public class OrderRealizationIncommingFlowConfiguration extends AbstractOrderRealizationFlowConfiguration{

    @Value("${newRealizationsEvent.topic.name}")
    private String topicNewRealizations;

    @Bean
    public IntegrationFlow amqpInboundFlow() {
        return IntegrationFlows.from(Amqp.inboundAdapter(rabbitMQConnection(),topicNewRealizations))
                .channel(c -> c.executor(Executors.newCachedThreadPool()))
                .transform(this::performUnmarshaling)
                .handle(m -> {
                    System.out.print(m.getPayload());
                    waitThread();
                })
                .get();
    }

    private void waitThread(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
