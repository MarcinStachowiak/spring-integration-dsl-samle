package com.ms.orderservice.configuration;

import com.ms.orderservice.configuration.integration.OrderServiceIntegrationConfiguration;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@ComponentScan("com.ms.orderservice")
@Import(OrderServiceIntegrationConfiguration.class)
public class OrderServiceApplicationConfiguration{
    private static final Logger logger = Logger.getLogger(OrderServiceApplicationConfiguration.class);

    public static void main(String[] args) {
              SpringApplication.run(OrderServiceApplicationConfiguration.class, args);
    }
}
