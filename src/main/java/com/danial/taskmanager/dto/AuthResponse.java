package com.danial.taskmanager.dto;
/**
     * DTO for authentication response
     * Send JWT token and user info back to frontend
*/ 
public class AuthResponse {

    private String token;
    private String type = "Bearer"; //Token type (Standard for JWT)
    private Long id;
    private String username;
    private String email;

    //Default Constructor
    public AuthResponse(){}

    //Constructor with param
    public AuthResponse(String token, Long id, String username, String email)
    {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    
}
