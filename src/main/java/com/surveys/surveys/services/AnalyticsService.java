package com.surveys.surveys.services;

import com.surveys.surveys.model.Analytics;
import java.util.List;
import java.util.Optional;

/**
 * Define las operaciones disponibles para la gestión de análisis de encuestas.
 * Esta interfaz proporciona métodos para crear, actualizar y consultar
 * análisis estadísticos de las encuestas.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface AnalyticsService {
    
    /**
     * Guarda o actualiza un análisis.
     *
     * @param analytics el análisis a guardar
     * @return el análisis guardado con su ID asignado
     * @throws IllegalArgumentException si analytics es null
     */
    Analytics saveAnalytics(Analytics analytics);

    /**
     * Busca un análisis por su ID.
     *
     * @param id identificador del análisis
     * @return Optional con el análisis si existe
     */
    Optional<Analytics> getAnalyticsById(String id);

    /**
     * Obtiene el análisis de una encuesta específica.
     *
     * @param surveyId identificador de la encuesta
     * @return Optional con el análisis si existe
     */
    Optional<Analytics> getAnalyticsBySurveyId(String surveyId);

    /**
     * Encuentra encuestas con alta tasa de finalización.
     *
     * @param minRate tasa mínima de finalización
     * @return lista de análisis que superan la tasa
     */
    List<Analytics> getHighPerformingSurveys(double minRate);

    /**
     * Compara el rendimiento entre versiones de una encuesta.
     *
     * @param currentVersionId ID de la versión actual
     * @param previousVersionId ID de la versión anterior
     * @return Optional con el análisis comparativo
     */
    Optional<Analytics> compareVersions(String currentVersionId, String previousVersionId);
}
