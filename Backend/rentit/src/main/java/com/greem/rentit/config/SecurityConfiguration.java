package com.greem.rentit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfiguration {

    private final String theAllowedOrigins = "http://localhost:3000";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disable cross-site request forgery
        http.csrf(csrf -> csrf.disable());

        // Protect endpoints at /api/<type>/secure/**
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/properties/secure/**","/api/reviews/secure/**", "api/payments/secure/**"
                        ,"api/bookings/secure/**").authenticated()
                        .anyRequest().permitAll()) // Allow all other requests without authentication
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt); // Use JWT for OAuth2 resource server

        // Add CORS filter
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin(theAllowedOrigins); // Add allowed origins
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
