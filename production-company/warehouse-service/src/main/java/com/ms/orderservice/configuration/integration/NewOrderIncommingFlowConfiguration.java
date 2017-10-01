package com.ms.orderservice.configuration.integration;

import com.ms.orderservice.ds.OrderItemDs;
import com.ms.orderservice.integration.xsd.NewOrderEvent;
import com.ms.orderservice.service.OrderItemMapper;
import com.ms.orderservice.service.OrderPreparationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Configuration
public class NewOrderIncommingFlowConfiguration extends AbstractWarehouseFlowConfiguration {
    private static final Logger logger = Logger.getLogger(NewOrderIncommingFlowConfiguration.class);

    @Value("${newOrderEvent.in.amqp.queue}")
    private String queueNewRealizations;

    @Autowired
    private OrderPreparationService orderPreparationService;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Bean
    public IntegrationFlow amqpInboundFlow() {
        return IntegrationFlows.from(Amqp.inboundAdapter(rabbitMQConnection(), queueNewRealizations))
                .channel(MessageChannels.executor(Executors.newCachedThreadPool()))
                .transform(this::performUnmarshaling)
                .publishSubscribeChannel(c -> c
                        .subscribe(s -> s
                                .<NewOrderEvent, Boolean>route(NewOrderEvent::isPriorityRealization, mapping -> mapping
                                        .subFlowMapping(false, sf -> sf
                                                .split(NewOrderEvent.class, NewOrderEvent::getItems)
                                                .transform(orderItemMapper::toOrderItemDs)
                                                .channel(MessageChannels.queue(10))
                                                .publishSubscribeChannel(c2 -> c2
                                                        .subscribe(s2 -> s2
                                                                .handle(m -> orderPreparationService.prepare((OrderItemDs) m.getPayload(), false)))
                                                        .subscribe(s2 -> s2
                                                                .aggregate(aggregator -> aggregator
                                                                        .outputProcessor(group -> group
                                                                                .getMessages()
                                                                                .stream()
                                                                                .map(m -> ((OrderItemDs) m.getPayload()).getItemName())
                                                                                .collect(Collectors.joining(","))))
                                                                .handle(m -> m.getPayload())))))))
                .get();
    }


    private void waitThread() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {                               // 11
        return Pollers
                .fixedDelay(1000)
                .taskExecutor(Executors.newCachedThreadPool())
                .get();
    }

}
