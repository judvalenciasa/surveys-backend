package com.surveys.surveys.exception;

/**
 * Códigos de error estandarizados para la API.
 */
public final class ErrorCodes {
    
    // Autenticación y Autorización
    public static final String AUTH_INVALID_CREDENTIALS = "AUTH_001";
    public static final String AUTH_ACCESS_DENIED = "AUTH_002";
    public static final String AUTH_TOKEN_EXPIRED = "AUTH_003";
    
    // Validación
    public static final String VALIDATION_FIELD_REQUIRED = "VAL_001";
    public static final String VALIDATION_FIELD_INVALID = "VAL_002";
    public static final String VALIDATION_EMAIL_FORMAT = "VAL_003";
    public static final String VALIDATION_PASSWORD_WEAK = "VAL_004";
    
    // Recursos
    public static final String RESOURCE_NOT_FOUND = "RES_001";
    public static final String RESOURCE_ALREADY_EXISTS = "RES_002";
    public static final String RESOURCE_FORBIDDEN = "RES_003";
    
    // Encuestas específicas
    public static final String SURVEY_NOT_FOUND = "SUR_001";
    public static final String SURVEY_ALREADY_ANSWERED = "SUR_002";
    public static final String SURVEY_EXPIRED = "SUR_003";
    
    // Sistema
    public static final String INTERNAL_SERVER_ERROR = "SYS_001";
    public static final String DATABASE_ERROR = "SYS_002";
    
    private ErrorCodes() {
        // Clase utilitaria
    }
} 