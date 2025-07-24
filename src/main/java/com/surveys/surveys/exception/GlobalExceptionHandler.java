package com.surveys.surveys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para respuestas consistentes.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de autenticación (401).
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            BadCredentialsException ex, 
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Error de autenticación: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja errores de autorización (403).
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, 
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Acceso denegado: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja errores de validación de datos (400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex, 
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja recursos no encontrados (404).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, 
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Recurso no encontrado: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja errores de validación personalizados (400).
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, 
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Error de validación: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones no capturadas (500).
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception ex, 
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "Error interno del servidor: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 