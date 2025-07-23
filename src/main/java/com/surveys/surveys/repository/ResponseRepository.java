package com.surveys.surveys.repository;

import com.surveys.surveys.model.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.Instant;
import java.util.List;

/**
 * Repositorio para la gestión de respuestas de encuestas en MongoDB.
 * Proporciona operaciones CRUD y consultas personalizadas para el manejo de respuestas.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Búsqueda por ID de encuesta</li>
 *   <li>Filtrado por rango de fechas</li>
 *   <li>Conteo de respuestas</li>
 *   <li>Obtención de últimas respuestas</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface ResponseRepository extends MongoRepository<Response, String> {
    
    /**
     * Busca todas las respuestas asociadas a una encuesta específica.
     *
     * @param surveyId ID de la encuesta
     * @return lista de respuestas de la encuesta
     */
    List<Response> findBySurveyId(String surveyId);
    
    /**
     * Busca respuestas enviadas dentro de un rango de fechas.
     *
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de respuestas en el rango especificado
     */
    List<Response> findBySubmittedAtBetween(Instant startDate, Instant endDate);
    
    /**
     * Busca respuestas de una encuesta específica en un rango de fechas.
     *
     * @param surveyId ID de la encuesta
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de respuestas que cumplen los criterios
     */
    List<Response> findBySurveyIdAndSubmittedAtBetween(
        String surveyId, Instant startDate, Instant endDate);
    
    /**
     * Cuenta el número total de respuestas para una encuesta.
     *
     * @param surveyId ID de la encuesta
     * @return número de respuestas
     */
    long countBySurveyId(String surveyId);
    
    /**
     * Obtiene las últimas respuestas de una encuesta, ordenadas por fecha.
     *
     * @param surveyId ID de la encuesta
     * @return lista de respuestas ordenadas por fecha descendente
     */
    @Query(sort = "{ submittedAt: -1 }")
    List<Response> findTopBySurveyId(String surveyId);
}
