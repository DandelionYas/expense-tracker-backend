package com.expense.common.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String rootUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String principalAttribute;
}