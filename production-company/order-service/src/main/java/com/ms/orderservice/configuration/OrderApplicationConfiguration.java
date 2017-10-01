package com.ms.orderservice.configuration;

import com.ms.orderservice.configuration.integration.OrderIntegrationConfiguration;
import com.ms.orderservice.service.impl.TestSender;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@ComponentScan("com.ms.orderservice")
@Import(OrderIntegrationConfiguration.class)
public class OrderApplicationConfiguration {
    private static final Logger logger = Logger.getLogger(OrderApplicationConfiguration.class);


    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OrderApplicationConfiguration.class, args);
        TestSender sender=run.getBean("testSender",TestSender.class);
        sender.send();
    }
}
