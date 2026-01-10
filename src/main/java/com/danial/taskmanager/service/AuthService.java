package com.danial.taskmanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danial.taskmanager.dto.AuthResponse;
import com.danial.taskmanager.dto.LoginRequest;
import com.danial.taskmanager.dto.RegisterRequest;
import com.danial.taskmanager.model.User;
import com.danial.taskmanager.repository.UserRepository;
import com.danial.taskmanager.security.JwtUtil;

/**
 * Authentication Service
 * Handles user registration and login business logic
 */

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Register a new user
     * 
     * @param request - Registration details(username, email, password)
     * @return Authresponse witn JWT token and user info
     * @throws RuntimeException if username or email already exists
     */

    public AuthResponse registerUser(RegisterRequest request){
        //1. check if username already exists
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Error: Username is already taken!");
        }

        //2. Check if email already exists
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Error: Email is already in use!");
        }
        
        //3. create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        //4. Encode password before saving (Never store plain text!)
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        //5. set user as enabled by default
        user.setEnabled(true);

        //6. save user to database
        User savedUser = userRepository.save(user);

        //7. Generate JWT token for the new user
        String token = jwtUtil.generateToken(savedUser.getUsername());
        
        //8. Return response with token and user info
        return new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail()
        );
    }

    /**
     * Login an existing user
     * 
     * @param request - login credentials(username, password)
     * @return AuthResponse with JWT token and user info
     * @throws BadCredentialsException if credentials are invalid
     */
    public AuthResponse loginUser(LoginRequest request){
        //1. Find user by username
        User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        //2. Check if account is enabaled
        if(!user.getEnabled()){
            throw new RuntimeException("Account is disabled. Please contact support");
        }

        //3. Validate password
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }

        //4. Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        //5. Return response with token and user info
        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }
}
