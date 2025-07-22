package com.surveys.surveys.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Clase que representa una respuesta de error estandarizada.
 * Esta clase se utiliza para enviar información detallada sobre errores al cliente.
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
     * Constructor para crear una respuesta de error.
     *
     * @param message mensaje de error
     */
    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo para crear una respuesta de error.
     *
     * @param message mensaje de error
     * @param status código de estado HTTP
     * @param path ruta de la petición que generó el error
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