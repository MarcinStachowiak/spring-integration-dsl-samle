package com.ms.notificationservice.configuration.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(OrderReadyIncommingFlowConfiguration.class)
@PropertySource("notification-integration-flow.properties")
public class NotificationIntegrationConfiguration {
}
