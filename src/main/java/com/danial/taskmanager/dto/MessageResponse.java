package com.danial.taskmanager.dto;
/**
 * DTO for simple message response
 * Used for success/error message
 */

public class MessageResponse {

    private String message;

    //default constructor
    public MessageResponse(){}

    //constructor witn param
    public MessageResponse(String message)
    {
        this.message = message;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
