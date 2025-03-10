package com.expense.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/users/login").permitAll()
                .requestMatchers("/api/users/signup").permitAll()
                .anyRequest().authenticated();

        httpSecurity
                .oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);

        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> {
            webSecurity.ignoring().requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
                    "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/swagger-ui.html", "/webjars/**", "/api-docs/**");
        };
    }
}