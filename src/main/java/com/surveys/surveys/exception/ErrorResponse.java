package com.surveys.surveys.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Respuesta de error estandarizada para la API.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class ErrorResponse {
    private String message;
    private int status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String path;

    /**
     * Constructor básico.
     */
    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo.
     */
    public ErrorResponse(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
} 