package com.surveys.surveys.exception;

import java.time.LocalDateTime;

public class SecurityErrorResponse {
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public SecurityErrorResponse(String error, String message, int status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
} 