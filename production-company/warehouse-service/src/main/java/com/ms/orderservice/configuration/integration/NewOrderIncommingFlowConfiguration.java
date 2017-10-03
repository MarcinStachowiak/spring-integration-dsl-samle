package com.ms.orderservice.configuration.integration;

import com.ms.orderservice.ds.OrderItemDs;
import com.ms.orderservice.integration.xsd.NewOrderEvent;
import com.ms.orderservice.service.OrderItemMapper;
import com.ms.orderservice.service.OrderPreparationService;
import com.ms.orderservice.service.OrderReadyMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
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

    @Autowired
    private OrderReadyMapper orderReadyMapper;

    @Bean
    public IntegrationFlow amqpNewOrderEventInboundFlow() {
        return IntegrationFlows.from(Amqp.inboundAdapter(rabbitMQConnection(), queueNewRealizations))
                .channel(MessageChannels.executor(Executors.newCachedThreadPool()))
                .transform(this::performNewOrderEventUnmarshaling)
                .publishSubscribeChannel(c -> c
                        .subscribe(s -> s
                                .<NewOrderEvent, Boolean>route(NewOrderEvent::isPriorityRealization, mapping -> mapping
                                        .subFlowMapping(false, sf -> sf
                                                .channel(IntegrationFlowKeys.NOT_PRIORITY_ORDER_PROCESSING_QUEUE))
                                        .subFlowMapping(true, sf -> sf
                                                .channel(IntegrationFlowKeys.PRIORITY_ORDER_PROCESSING_QUEUE))))
                        .subscribe(s -> s
                                .transform(orderReadyMapper::toOrderReady)
                                .channel(IntegrationFlowKeys.NEW_ORDER_OUT_CHANNEL)))
                .get();
    }

    @Bean
    public IntegrationFlow processNewPriorityOrders() {
        IntegrationFlowBuilder builder = IntegrationFlows.from(IntegrationFlowKeys.PRIORITY_ORDER_PROCESSING_QUEUE);
        return newOrderEventProcessingFlow(builder, true)
                .get();
    }

    @Bean
    public IntegrationFlow processNewNotPriorityOrders() {
        IntegrationFlowBuilder builder = IntegrationFlows.from(IntegrationFlowKeys.NOT_PRIORITY_ORDER_PROCESSING_QUEUE);
        return newOrderEventProcessingFlow(builder, false)
                .get();
    }

    private IntegrationFlowBuilder newOrderEventProcessingFlow(IntegrationFlowBuilder builder, boolean priorityQueue) {
        return builder
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
                                .handle(m -> System.out.println(m.getPayload()))));
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

    @Bean
    public JAXBContext jaxbContextNewOrderEvent() throws JAXBException {
        return JAXBContext.newInstance(NewOrderEvent.class);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Unmarshaller newOrderEventUnmarshaler() throws JAXBException {
        return jaxbContextNewOrderEvent().createUnmarshaller();
    }

    protected NewOrderEvent performNewOrderEventUnmarshaling(String xml) {
        NewOrderEvent newOrderEvent = null;
        try {
            newOrderEvent = (NewOrderEvent) newOrderEventUnmarshaler().unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return newOrderEvent;
    }

}
