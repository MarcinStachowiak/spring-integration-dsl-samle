package com.ms.orderservice.configuration.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({OrderRealizationIncommingFlowConfiguration.class, OrderRealizationOutgoingFlowConfiguration.class})
@PropertySource("integration-flow.properties")
public class OrderServiceIntegrationConfiguration {
}
