package com.danial.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Intercepts every HTTP request to validate JWT tokens
 * Extends OncePerRequestFilter to ensure it runs once per request
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Main filter method - called for every request
     * 
     * @param request - HTTP request
     * @param response - HTTP response
     * @param filterChain - Chain of filters
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Step 1: Extract Authorization header
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Step 2: Check if header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract token (remove "Bearer " prefix)
            jwt = authorizationHeader.substring(7);
            
            try {
                // Step 3: Extract username from token
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token is invalid or expired
                System.out.println("JWT Token validation error: " + e.getMessage());
            }
        }

        // Step 4: If we have a username and user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Step 5: Load user details from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Step 6: Validate token
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                
                // Step 7: Create authentication token
                UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,           // Principal (the user)
                        null,                  // Credentials (not needed after authentication)
                        userDetails.getAuthorities()  // Roles/Permissions
                    );

                // Step 8: Set additional details (IP address, session ID, etc.)
                authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Step 9: Set authentication in SecurityContext
                // This tells Spring Security: "This user is authenticated!"
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Step 10: Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }
}