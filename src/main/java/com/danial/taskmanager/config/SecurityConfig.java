package com.danial.taskmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.danial.taskmanager.security.JwtAuthenticationFilter;

import java.util.Arrays;

/**
 * Security Configuration
 * Configures Spring Security for JWT authentication
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * Configure security filter chain
     * This is the main security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        //1.Disable CSRF(Cross-Site Request Forgery)
        //Not needed for stateless JWT authentication
        .csrf(csrf -> csrf.disable())
        //2. Configure authorization rules
        .authorizeHttpRequests(auth -> auth
            //public endpoints - anyone can access
            .requestMatchers(
                "/api/register", "/api/login"
            ).permitAll()
            //ALl other endpoints require authentication
            .anyRequest().authenticated()
        )
        //3. Set session management to StateLess
        //No sessions - we use JWT tokens instead
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        //4. Add our custom JWT Filter
        //Run it Before the UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * CORS Configuration
     * Allows frontend (React) to communicate with backend (Spring Boot)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from your React frontend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        
        // Allow all HTTP methods (GET, POST, PUT, DELETE, PATCH, OPTIONS)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Allow all headers (including Authorization header with JWT token)
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Expose Authorization header to frontend
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);
        
        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Authentication Manager Bean
     * Used for authenticating users during login
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}