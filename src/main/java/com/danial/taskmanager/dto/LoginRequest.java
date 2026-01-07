package com.danial.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for User Login requests
 * Recieves username and password from frontend
 */

public class LoginRequest{
    @NotBlank(message= "Username is required")
    private String username;

    @NotBlank(message= "Password is required")
    private String password;

    // Default contstructor (required for JSON deserialization)
    public LoginRequest(){

    }

    // Constructor with parameters
    public LoginRequest(String username, String password)
    {
        this.username = username;
        this.password =  password;
    }

      // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}