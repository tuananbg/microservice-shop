package com.progaming.tutorial.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "keycloak")
public class AuthProperties {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String username;
    private String password;
}