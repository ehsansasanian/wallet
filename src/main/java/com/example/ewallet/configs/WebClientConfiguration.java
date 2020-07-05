package com.example.ewallet.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author EhSan
 */
@Configuration
public class WebClientConfiguration {

    @Bean("WebClientCustom")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
