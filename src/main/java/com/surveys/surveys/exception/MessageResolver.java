package com.surveys.surveys.exception;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Resuelve mensajes personalizados para diferentes tipos de errores.
 */
@Component
public class MessageResolver {
    
    private static final Map<String, UserMessage> messages = new HashMap<>();
    
    static {
        // Mensajes de autenticación
        messages.put(ErrorCodes.AUTH_INVALID_CREDENTIALS, new UserMessage(
            "Credenciales inválidas. Por favor, verifica tu email y contraseña.",
            "Las credenciales proporcionadas no coinciden con ningún usuario registrado."
        ));
        
        messages.put(ErrorCodes.AUTH_ACCESS_DENIED, new UserMessage(
            "No tienes permisos para acceder a este recurso.",
            "El usuario no tiene los roles necesarios para acceder al endpoint solicitado."
        ));
        
        // Mensajes de validación
        messages.put(ErrorCodes.VALIDATION_EMAIL_FORMAT, new UserMessage(
            "El formato del email no es válido. Por favor, introduce un email correcto.",
            "El campo email no cumple con el formato requerido (RFC 5322)."
        ));
        
        messages.put(ErrorCodes.VALIDATION_PASSWORD_WEAK, new UserMessage(
            "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas y números.",
            "La contraseña no cumple con los criterios de seguridad establecidos."
        ));
        
        // Mensajes de encuestas
        messages.put(ErrorCodes.SURVEY_NOT_FOUND, new UserMessage(
            "La encuesta solicitada no existe o ha sido eliminada.",
            "No se encontró ninguna encuesta con el ID proporcionado."
        ));
        
        messages.put(ErrorCodes.SURVEY_ALREADY_ANSWERED, new UserMessage(
            "Ya has respondido esta encuesta anteriormente.",
            "El usuario ya tiene una respuesta registrada para esta encuesta."
        ));
        
        // Mensaje por defecto
        messages.put("DEFAULT", new UserMessage(
            "Ha ocurrido un error inesperado. Por favor, inténtalo más tarde.",
            "Error no catalogado en el sistema."
        ));
    }
    
    public UserMessage getMessage(String errorCode) {
        return messages.getOrDefault(errorCode, messages.get("DEFAULT"));
    }
    
    public static class UserMessage {
        private final String userMessage;
        private final String developerMessage;
        
        public UserMessage(String userMessage, String developerMessage) {
            this.userMessage = userMessage;
            this.developerMessage = developerMessage;
        }
        
        public String getUserMessage() { return userMessage; }
        public String getDeveloperMessage() { return developerMessage; }
    }
} 