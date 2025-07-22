package com.surveys.surveys.model;

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
 * <p>Ejemplo de uso:
 * <pre>
 * Survey survey = new Survey();
 * survey.setStatus(SurveyStatus.CREADA);
 * 
 * // Cuando la encuesta está lista para recibir respuestas
 * survey.setStatus(SurveyStatus.PUBLICADA);
 * 
 * // Cuando se finaliza la recolección de respuestas
 * survey.setStatus(SurveyStatus.CERRADA);
 * </pre>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public enum SurveyStatus {
    /**
     * Estado inicial de una encuesta.
     * En este estado, la encuesta está en proceso de configuración
     * y no está disponible para recibir respuestas.
     */
    CREADA,

    /**
     * Estado que indica que la encuesta está activa.
     * En este estado, la encuesta está disponible públicamente
     * y puede recibir respuestas de los participantes.
     */
    PUBLICADA,

    /**
     * Estado final de una encuesta.
     * En este estado, la encuesta ha finalizado y ya no acepta
     * nuevas respuestas. Los datos pueden ser consultados pero
     * no modificados.
     */
    CERRADA;

    /**
     * Verifica si la encuesta puede ser modificada.
     * Solo las encuestas en estado CREADA pueden ser modificadas.
     *
     * @return true si la encuesta puede ser modificada, false en caso contrario
     */
    public boolean isEditable() {
        return this == CREADA;
    }

    /**
     * Verifica si la encuesta puede recibir respuestas.
     * Solo las encuestas en estado PUBLICADA pueden recibir respuestas.
     *
     * @return true si la encuesta puede recibir respuestas, false en caso contrario
     */
    public boolean canReceiveResponses() {
        return this == PUBLICADA;
    }

    /**
     * Verifica si la encuesta está finalizada.
     * Las encuestas en estado CERRADA se consideran finalizadas.
     *
     * @return true si la encuesta está finalizada, false en caso contrario
     */
    public boolean isClosed() {
        return this == CERRADA;
    }

    /**
     * Obtiene el siguiente estado válido en el flujo normal de una encuesta.
     *
     * @return el siguiente estado o null si es el estado final
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
     * Verifica si es posible transicionar al estado especificado.
     *
     * @param nextStatus el estado al que se quiere transicionar
     * @return true si la transición es válida, false en caso contrario
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