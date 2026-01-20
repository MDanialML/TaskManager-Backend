package com.danial.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danial.taskmanager.dto.AuthResponse;
import com.danial.taskmanager.dto.LoginRequest;
import com.danial.taskmanager.dto.MessageResponse;
import com.danial.taskmanager.dto.RegisterRequest;
import com.danial.taskmanager.service.AuthService;

import jakarta.validation.Valid;

/**
 * Authentication Controller
 * Handles user registration and login
 */

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * POST /api/auth/register
     * @param request - Registration details
     * @return AuthResponse with JWT token
     */

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request){
        try{
            AuthResponse response = authService.registerUser(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Login existing user
     * Post /api/auth/login
     * 
     * @param request - login credentials
     * @return AuthResponse with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request){
        try {
            AuthResponse response = authService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new MessageResponse("Invalid username or password"));
        }catch(RuntimeException e){
            return ResponseEntity
            .badRequest()
            .body(new MessageResponse(e.getMessage()));
        }
    }

}
