package com.example.myappbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "paypal")
public class PaypalProperties {
    private String clientId;
    private String clientSecret;
    private String mode;
}