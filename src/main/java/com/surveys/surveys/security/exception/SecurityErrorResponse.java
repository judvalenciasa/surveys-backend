package com.surveys.surveys.security.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Clase que representa una respuesta de error de seguridad estandarizada.
 * Se utiliza para proporcionar información detallada sobre errores
 * relacionados con la seguridad de la aplicación.
 *
 * <p>La respuesta incluye:
 * <ul>
 *   <li>Mensaje de error</li>
 *   <li>Código de estado HTTP</li>
 *   <li>Marca de tiempo del error</li>
 *   <li>Detalles adicionales (opcional)</li>
 *   <li>Ruta de la petición</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class SecurityErrorResponse {
    
    /** Mensaje descriptivo del error */
    private String message;
    
    /** Código de estado HTTP */
    private int status;
    
    /** Marca de tiempo cuando ocurrió el error */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /** Detalles adicionales del error */
    private String details;
    
    /** Ruta donde ocurrió el error */
    private String path;

    /**
     * Constructor para crear una respuesta de error básica.
     *
     * @param message mensaje de error
     * @param status código de estado HTTP
     */
    public SecurityErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo para crear una respuesta de error detallada.
     *
     * @param message mensaje de error
     * @param status código de estado HTTP
     * @param details detalles adicionales del error
     * @param path ruta donde ocurrió el error
     */
    public SecurityErrorResponse(String message, int status, String details, String path) {
        this(message, status);
        this.details = details;
        this.path = path;
    }

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
} 