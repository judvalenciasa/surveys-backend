package com.surveys.surveys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando falla la validación de datos.
 * Esta excepción se mapea automáticamente a una respuesta HTTP 400.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message mensaje descriptivo del error
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa del error.
     *
     * @param message mensaje descriptivo del error
     * @param cause causa original del error
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 