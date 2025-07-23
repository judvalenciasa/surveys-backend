package com.surveys.surveys.services;

import com.surveys.surveys.model.Response;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Define las operaciones disponibles para la gestión de respuestas de encuestas.
 * Esta interfaz proporciona los métodos necesarios para crear, leer,
 * actualizar y eliminar respuestas, así como funcionalidades específicas
 * de búsqueda y análisis.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface ResponseService {
    
    /**
     * Guarda una nueva respuesta o actualiza una existente.
     *
     * @param response la respuesta a guardar
     * @return la respuesta guardada con su ID asignado
     */
    Response saveResponse(Response response);

    /**
     * Busca una respuesta por su identificador.
     *
     * @param id identificador de la respuesta
     * @return Optional con la respuesta si existe
     */
    Optional<Response> getResponseById(String id);

    /**
     * Obtiene todas las respuestas almacenadas.
     *
     * @return lista de todas las respuestas
     */
    List<Response> getAllResponses();

    /**
     * Elimina una respuesta por su identificador.
     *
     * @param id identificador de la respuesta a eliminar
     */
    void deleteResponse(String id);
    
    /**
     * Obtiene todas las respuestas de una encuesta específica.
     *
     * @param surveyId identificador de la encuesta
     * @return lista de respuestas de la encuesta
     */
    List<Response> getResponsesBySurvey(String surveyId);

    /**
     * Busca respuestas en un rango de fechas.
     *
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de respuestas en el rango
     */
    List<Response> getResponsesByDateRange(Instant startDate, Instant endDate);

    /**
     * Busca respuestas de una encuesta en un rango de fechas.
     *
     * @param surveyId identificador de la encuesta
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de respuestas que cumplen los criterios
     */
    List<Response> getResponsesBySurveyAndDateRange(
        String surveyId, Instant startDate, Instant endDate);

    /**
     * Obtiene el número total de respuestas de una encuesta.
     *
     * @param surveyId identificador de la encuesta
     * @return número de respuestas
     */
    long getResponseCount(String surveyId);

    /**
     * Obtiene las últimas respuestas de una encuesta.
     *
     * @param surveyId identificador de la encuesta
     * @param limit número máximo de respuestas a retornar
     * @return lista de últimas respuestas
     */
    List<Response> getLatestResponses(String surveyId, int limit);
}
