package com.surveys.surveys.repository;

import com.surveys.surveys.model.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.Instant;
import java.util.List;

/**
 * Repositorio para gestión de respuestas de encuestas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface ResponseRepository extends MongoRepository<Response, String> {
    
    /**
     * Busca respuestas por ID de encuesta.
     */
    List<Response> findBySurveyId(String surveyId);
    
    /**
     * Busca respuestas por rango de fechas.
     */
    List<Response> findBySubmittedAtBetween(Instant startDate, Instant endDate);
    
    /**
     * Busca respuestas por encuesta y rango de fechas.
     */
    List<Response> findBySurveyIdAndSubmittedAtBetween(
        String surveyId, Instant startDate, Instant endDate);
    
    /**
     * Cuenta respuestas de una encuesta.
     */
    long countBySurveyId(String surveyId);
    
    /**
     * Obtiene últimas respuestas ordenadas por fecha.
     */
    @Query(sort = "{ submittedAt: -1 }")
    List<Response> findTopBySurveyId(String surveyId);
}
