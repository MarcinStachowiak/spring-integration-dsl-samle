package com.ms.orderservice.configuration.integration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderRealizationOutgoingFlowConfiguration extends AbstractOrderRealizationFlowConfiguration{
  // @Bean(name=IntegrationFlowKeys.CHANNAL_REALIZATION_FINISHED_EVENT_OUT)
  //  public MessageChannel createAmqpOutboundInput(){
   //     return MessageChannels.direct().get();
 //   }


   // @Bean
  //  public IntegrationFlow amqpOutboundFlow() {
  //      return IntegrationFlows.from(createAmqpOutboundInput())
  //              .handle(Amqp.outboundAdapter(createAmqpTemplate()).exchangeName("exchagetmp").routingKey("headers.routingKey"))
  //              .get();
  //  }

}
