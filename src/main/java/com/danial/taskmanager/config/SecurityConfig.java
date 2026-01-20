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

import com.danial.taskmanager.security.JwtAuthenticationFilter;

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
        //1.Diable CSRF(Cross-Site Request Forgery)
        //Not needed for stateless JWT authentication
        .csrf(csrf -> csrf.disable())
        //2. Configure authorization rules
        .authorizeHttpRequests(auth -> auth
            //public endpoints - anyone can access
            .requestMatchers(
                "/api/auth/**", //All auth endpoints(login, register)
                "/error"        //Error endpoint
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
     * Authentication Manager Bean
     * Used for authenticating users during login
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
