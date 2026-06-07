package com.sync.itk65.chatbot.dto;

/**
 * DTO nhận request từ Frontend Chatbox
 */
public class ChatRequest {

    private String message;

    public ChatRequest() {}

    public ChatRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
