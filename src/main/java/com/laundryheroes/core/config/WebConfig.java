// src/main/java/com/laundryheroes/config/WebConfig.java
package com.laundryheroes.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(
                            "http://localhost:3000",
                            "https://ad90.laundryheroes.com",
                            "https://s3.us-east-1.amazonaws.com" // your future prod admin
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization")
                        .allowCredentials(false) // we're using Authorization: Bearer, not cookies
                        .maxAge(3600);
            }
        };
    }
}
