package com.ms.notificationservice.configuration;

import com.ms.notificationservice.configuration.integration.NotificationIntegrationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@ComponentScan("com.ms.warehouseservice")
@Import(NotificationIntegrationConfiguration.class)
public class NotificationApplicationConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplicationConfiguration.class, args);
    }
}

