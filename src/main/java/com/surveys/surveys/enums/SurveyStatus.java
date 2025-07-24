package com.surveys.surveys.enums;

/**
 * Estados del ciclo de vida de una encuesta.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public enum SurveyStatus {
    /** Estado inicial - en configuración */
    CREADA,

    /** Estado activo - acepta respuestas */
    PUBLICADA,

    /** Estado final - no acepta respuestas */
    CERRADA;

    /**
     * Verifica si la encuesta puede ser editada.
     */
    public boolean isEditable() {
        return this == CREADA;
    }

    /**
     * Verifica si la encuesta puede recibir respuestas.
     */
    public boolean canReceiveResponses() {
        return this == PUBLICADA;
    }

    /**
     * Verifica si la encuesta está cerrada.
     */
    public boolean isClosed() {
        return this == CERRADA;
    }

    /**
     * Obtiene el siguiente estado en el flujo.
     */
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

    /**
     * Verifica si es posible transicionar al estado dado.
     */
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