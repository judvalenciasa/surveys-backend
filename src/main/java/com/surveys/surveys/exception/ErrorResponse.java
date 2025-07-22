package com.surveys.surveys.exception;

public class ErrorResponse {
    private String message;
    private long timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
} 