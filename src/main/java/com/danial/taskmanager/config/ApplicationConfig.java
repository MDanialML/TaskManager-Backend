package com.danial.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application Configuration
 * Defines beans that can be injected throughout the application
 */

@Configuration
public class ApplicationConfig {

    /**
     * Password encoder bean
     * Uses BCrypt hashing algorithm
     * @return BCryptPasswordEncoder instance
     */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
