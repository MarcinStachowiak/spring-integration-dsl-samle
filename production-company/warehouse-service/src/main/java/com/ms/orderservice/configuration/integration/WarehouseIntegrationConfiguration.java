package com.ms.orderservice.configuration.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({NewOrderIncommingFlowConfiguration.class})
@PropertySource("warehouse-integration-flow.properties")
public class WarehouseIntegrationConfiguration {
}
