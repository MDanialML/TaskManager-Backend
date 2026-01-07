package com.danial.taskmanager.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for user registration request
 * Recieves username, email, and password from frontend
 */

public class RegisterRequest{

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email shoudl be a valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 Characters")
    private String password;

    //Default Constructor
    public RegisterRequest(){}

    //Constructor with parameters
    public RegisterRequest(String username, String email, String password){
    
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}