package com.surveys.surveys.repository;

import com.surveys.surveys.model.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de análisis de encuestas en MongoDB.
 * Proporciona operaciones CRUD y consultas personalizadas para el análisis
 * de datos de encuestas.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Búsqueda por ID de encuesta</li>
 *   <li>Análisis de tasas de finalización</li>
 *   <li>Comparación entre versiones</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
    
    /**
     * Busca el análisis asociado a una encuesta específica.
     *
     * @param surveyId ID de la encuesta
     * @return Optional con el análisis si existe
     */
    Optional<Analytics> findBySurveyId(String surveyId);

    /**
     * Encuentra análisis con tasa de finalización superior al umbral especificado.
     *
     * @param rate tasa de finalización mínima
     * @return lista de análisis que cumplen el criterio
     */
    @Query("{ 'overview.completionRate': { $gte: ?0 } }")
    List<Analytics> findByCompletionRateGreaterThan(double rate);

    /**
     * Busca análisis que comparan con una versión específica.
     *
     * @param versionId ID de la versión anterior
     * @return lista de análisis que comparan con esa versión
     */
    List<Analytics> findByVersionComparisonPreviousVersionId(String versionId);
}
