package com.surveys.surveys.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Respuesta de error estandarizada para la API.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class ErrorResponse {
    private String message;
    private String errorCode;
    private int status;
    private String userMessage; // Mensaje amigable para el usuario
    private String developerMessage; // Mensaje técnico para desarrolladores
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String path;
    private List<ValidationError> validationErrors; // Para errores de validación múltiples
    private Map<String, Object> metadata; // Información adicional

    /**
     * Constructor básico.
     */
    public ErrorResponse(String message) {
        this.message = message;
        this.userMessage = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo para mensajes personalizados.
     */
    public ErrorResponse(String errorCode, String message, String userMessage, 
                        String developerMessage, int status, String path) {
        this.errorCode = errorCode;
        this.message = message;
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getDeveloperMessage() { return developerMessage; }
    public void setDeveloperMessage(String developerMessage) { this.developerMessage = developerMessage; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<ValidationError> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(List<ValidationError> validationErrors) { this.validationErrors = validationErrors; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    /**
     * Clase interna para errores de validación específicos.
     */
    public static class ValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
        
        public ValidationError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }
        
        // Getters y setters
        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Object getRejectedValue() { return rejectedValue; }
        public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }
    }
} 