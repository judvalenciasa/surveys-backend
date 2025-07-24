package com.surveys.surveys.services;

import com.surveys.surveys.model.Response;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de respuestas de encuestas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface ResponseService {
    
    /**
     * Guarda una respuesta nueva o actualiza existente.
     */
    Response saveResponse(Response response);

    /**
     * Busca respuesta por ID.
     */
    Optional<Response> getResponseById(String id);

    /**
     * Obtiene todas las respuestas.
     */
    List<Response> getAllResponses();

    /**
     * Elimina respuesta por ID.
     */
    void deleteResponse(String id);
    
    /**
     * Obtiene respuestas de una encuesta específica.
     */
    List<Response> getResponsesBySurvey(String surveyId);

    /**
     * Busca respuestas por rango de fechas.
     */
    List<Response> getResponsesByDateRange(Instant startDate, Instant endDate);

    /**
     * Busca respuestas por encuesta y rango de fechas.
     */
    List<Response> getResponsesBySurveyAndDateRange(
        String surveyId, Instant startDate, Instant endDate);

    /**
     * Cuenta respuestas de una encuesta.
     */
    long getResponseCount(String surveyId);

    /**
     * Obtiene últimas respuestas de una encuesta.
     */
    List<Response> getLatestResponses(String surveyId, int limit);
}
