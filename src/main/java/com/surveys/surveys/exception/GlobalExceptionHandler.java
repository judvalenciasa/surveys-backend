package com.surveys.surveys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest; // Corregido para Spring Boot 3.x

import java.util.ArrayList;
import java.util.List;

/**
 * Manejador global de excepciones para respuestas consistentes.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageResolver messageResolver;

    /**
     * Maneja errores de autenticación (401).
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            BadCredentialsException ex,
            HttpServletRequest request) {

        MessageResolver.UserMessage userMessage = messageResolver.getMessage(ErrorCodes.AUTH_INVALID_CREDENTIALS);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodes.AUTH_INVALID_CREDENTIALS,
                userMessage.getUserMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja errores de autorización (403).
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {

        MessageResolver.UserMessage userMessage = messageResolver.getMessage(ErrorCodes.AUTH_ACCESS_DENIED);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodes.AUTH_ACCESS_DENIED,
                userMessage.getUserMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja errores de validación de datos (400) con mensajes detallados.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodes.VALIDATION_FIELD_INVALID,
                "Los datos enviados contienen errores de validación.",
                "Por favor, corrige los errores en los campos marcados e inténtalo de nuevo.",
                "Errores de validación en campos del formulario.",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI());

        // Agregar errores de validación específicos
        List<ErrorResponse.ValidationError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> validationErrors.add(new ErrorResponse.ValidationError(
                error.getField(),
                error.getDefaultMessage(),
                error.getRejectedValue())));
        errorResponse.setValidationErrors(validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja recursos no encontrados (404).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        MessageResolver.UserMessage userMessage = messageResolver.getMessage(ErrorCodes.RESOURCE_NOT_FOUND);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodes.RESOURCE_NOT_FOUND,
                userMessage.getUserMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja errores específicos de encuestas no encontradas (404).
     */
    @ExceptionHandler(SurveyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleSurveyNotFoundException(
            SurveyNotFoundException ex,
            HttpServletRequest request) {

        MessageResolver.UserMessage userMessage = messageResolver.getMessage(ex.getErrorCode());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode(),
                userMessage.getUserMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja errores de validación personalizados con códigos específicos.
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        // Si la excepción incluye un código de error específico, úsalo
        String errorCode = extractErrorCode(ex.getMessage());
        MessageResolver.UserMessage userMessage = messageResolver.getMessage(errorCode);

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode,
                userMessage.getUserMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Extrae el código de error del mensaje si está presente.
     */
    private String extractErrorCode(String message) {
        // Buscar si el mensaje contiene un código de error al inicio
        if (message != null && message.contains(":")) {
            String possibleCode = message.split(":")[0];
            if (possibleCode.matches("[A-Z]{3}_\\d{3}")) {
                return possibleCode;
            }
        }
        return ErrorCodes.VALIDATION_FIELD_INVALID;
    }

    /**
     * Maneja excepciones no capturadas (500).
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception ex,
            HttpServletRequest request) {

        MessageResolver.UserMessage userMessage = messageResolver.getMessage(ErrorCodes.INTERNAL_SERVER_ERROR);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodes.INTERNAL_SERVER_ERROR,
                userMessage.getUserMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        MessageResolver.UserMessage userMessage = messageResolver.getMessage(ErrorCodes.VALIDATION_FIELD_INVALID);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodes.VALIDATION_FIELD_INVALID,
                ex.getMessage(),
                userMessage.getUserMessage(),
                userMessage.getDeveloperMessage() + " - " + ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}