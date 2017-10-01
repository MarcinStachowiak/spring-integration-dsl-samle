package com.ms.orderservice.configuration.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({ OrderOutgoingFlowConfiguration.class})
@PropertySource("integration-flow.properties")
public class OrderIntegrationConfiguration {
}
