package com.surveys.surveys.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * Manejador global de excepciones de seguridad.
 * Intercepta y procesa todas las excepciones relacionadas con la seguridad
 * para proporcionar respuestas de error consistentes.
 *
 * <p>Maneja los siguientes tipos de excepciones:
 * <ul>
 *   <li>Errores de autenticación</li>
 *   <li>Errores de autorización</li>
 *   <li>Errores de token JWT</li>
 *   <li>Errores de validación de seguridad</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@RestControllerAdvice
public class SecurityExceptionHandler {

    /**
     * Maneja errores de credenciales inválidas.
     *
     * @param ex excepción de credenciales inválidas
     * @param request petición web
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<SecurityErrorResponse> handleBadCredentials(
            BadCredentialsException ex, 
            WebRequest request) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Credenciales inválidas",
            HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja errores de acceso denegado.
     *
     * @param ex excepción de acceso denegado
     * @param request petición web
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<SecurityErrorResponse> handleAccessDenied(
            AccessDeniedException ex, 
            WebRequest request) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Acceso denegado",
            HttpStatus.FORBIDDEN.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja errores de token JWT expirado.
     *
     * @param ex excepción de token expirado
     * @param request petición web
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<SecurityErrorResponse> handleExpiredJwt(
            ExpiredJwtException ex, 
            WebRequest request) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Token expirado",
            HttpStatus.UNAUTHORIZED.value(),
            "El token de autenticación ha expirado",
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja errores de token JWT malformado.
     *
     * @param ex excepción de token malformado
     * @param request petición web
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<SecurityErrorResponse> handleInvalidJwt(
            Exception ex, 
            WebRequest request) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Token inválido",
            HttpStatus.UNAUTHORIZED.value(),
            "El token de autenticación es inválido",
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja errores generales de autenticación.
     *
     * @param ex excepción de autenticación
     * @param request petición web
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<SecurityErrorResponse> handleAuthentication(
            AuthenticationException ex, 
            WebRequest request) {
        SecurityErrorResponse error = new SecurityErrorResponse(
            "Error de autenticación",
            HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
} 