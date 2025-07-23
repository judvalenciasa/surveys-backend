package com.surveys.surveys.enums;

/**
 * Enumeración que representa los posibles estados de una encuesta.
 * Define el ciclo de vida completo de una encuesta desde su creación
 * hasta su cierre.
 *
 * <p>Los estados posibles son:
 * <ul>
 *   <li>CREADA - Estado inicial cuando se crea una nueva encuesta</li>
 *   <li>PUBLICADA - La encuesta está activa y disponible para respuestas</li>
 *   <li>CERRADA - La encuesta ha finalizado y no acepta más respuestas</li>
 * </ul>
 *
 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public enum SurveyStatus {
    /**
     * Estado inicial de una encuesta en proceso de configuración
     */
    CREADA,

    /**
     * Estado que indica que la encuesta está activa y puede recibir respuestas
     */
    PUBLICADA,

    /**
     * Estado final de una encuesta que ya no acepta respuestas
     */
    CERRADA;

    public boolean isEditable() {
        return this == CREADA;
    }

    public boolean canReceiveResponses() {
        return this == PUBLICADA;
    }

    public boolean isClosed() {
        return this == CERRADA;
    }

    public SurveyStatus getNextStatus() {
        switch (this) {
            case CREADA:
                return PUBLICADA;
            case PUBLICADA:
                return CERRADA;
            default:
                return null;
        }
    }

    public boolean canTransitionTo(SurveyStatus nextStatus) {
        if (nextStatus == null) return false;
        
        switch (this) {
            case CREADA:
                return nextStatus == PUBLICADA;
            case PUBLICADA:
                return nextStatus == CERRADA;
            case CERRADA:
                return false;
            default:
                return false;
        }
    }
} 