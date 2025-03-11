package com.expense.auth.configs;

import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestsConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, SslBundles sslBundles) {
        return builder.sslBundle(sslBundles.getBundle("secure-client")).build();
    }
}
