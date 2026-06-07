package com.sync.itk65.chatbot.dto;

/**
 * DTO trả về phản hồi AI cho Frontend Chatbox
 */
public class ChatResponse {

    private String reply;
    private boolean success;
    private String error;

    public ChatResponse() {
    }

    public static ChatResponse ok(String reply) {
        ChatResponse r = new ChatResponse();
        r.reply = reply;
        r.success = true;
        return r;
    }

    public static ChatResponse fail(String error) {
        ChatResponse r = new ChatResponse();
        r.error = error;
        r.success = false;
        return r;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
