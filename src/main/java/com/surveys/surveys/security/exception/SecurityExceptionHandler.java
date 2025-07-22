package com.surveys.surveys.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<SecurityErrorResponse> handleAuthenticationException(
            AuthenticationException ex) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Error de autenticación",
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<SecurityErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Acceso denegado",
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
} 