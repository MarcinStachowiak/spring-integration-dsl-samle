package com.ms.orderservice.configuration;

import com.ms.orderservice.configuration.integration.WarehouseIntegrationConfiguration;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@ComponentScan("com.ms.orderservice")
@Import(WarehouseIntegrationConfiguration.class)
public class WarehouseApplicationConfiguration {
    private static final Logger logger = Logger.getLogger(WarehouseApplicationConfiguration.class);


    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplicationConfiguration.class, args);
    }
}
