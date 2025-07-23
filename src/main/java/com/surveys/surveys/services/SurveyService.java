package com.surveys.surveys.services;

import com.surveys.surveys.model.Survey;
import com.surveys.surveys.model.SurveyStatus;
import com.surveys.surveys.model.Branding;
import com.surveys.surveys.model.Question;
import java.util.List;
import java.util.Optional;

/**
 * Define las operaciones disponibles para la gestión de encuestas.
 * Esta interfaz proporciona los métodos necesarios para crear, leer,
 * actualizar y eliminar encuestas, así como funcionalidades específicas
 * como publicación y duplicación.
 *
 * @author TuNombre
 * @version 1.0
 * @since 2024-03-22
 */
public interface SurveyService {
    
    /**
     * Guarda una nueva encuesta o actualiza una existente.
     *
     * @param survey la encuesta a guardar
     * @return la encuesta guardada con su ID asignado
     */
    Survey saveSurvey(Survey survey);

    /**
     * Obtiene una lista de encuestas filtrada por estado y tipo.
     *
     * @param status estado de las encuestas a buscar
     * @param isTemplate si es true, busca solo plantillas
     * @return lista de encuestas que coinciden con los criterios
     */
    List<Survey> getSurveys(SurveyStatus status, Boolean isTemplate);

    /**
     * Busca una encuesta por su identificador.
     *
     * @param id identificador de la encuesta
     * @return Optional con la encuesta si existe
     */
    Optional<Survey> getSurveyById(String id);

    /**
     * Actualiza los datos de una encuesta existente.
     *
     * @param id identificador de la encuesta a actualizar
     * @param survey los nuevos datos de la encuesta
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> updateSurvey(String id, Survey survey);

    /**
     * Elimina una encuesta por su identificador.
     *
     * @param id identificador de la encuesta a eliminar
     * @return true si la encuesta se eliminó, false si no se encontró
     */
    boolean deleteSurvey(String id);

    /**
     * Busca encuestas por nombre y administrador.
     *
     * @param name nombre de la encuesta
     * @param adminId identificador del administrador
     * @return lista de encuestas que coinciden con los criterios
     */
    List<Survey> searchSurveys(String name, String adminId);

    /**
     * Actualiza el estado de una encuesta.
     *
     * @param id identificador de la encuesta
     * @param status nuevo estado de la encuesta
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> updateSurveyStatus(String id, SurveyStatus status);

    /**
     * Publica una encuesta, cambiando su estado a "Publicado".
     *
     * @param id identificador de la encuesta a publicar
     * @return Optional con la encuesta publicada si se encontró
     */
    Optional<Survey> publishSurvey(String id);

    /**
     * Cierra una encuesta, cambiando su estado a "Cerrado".
     *
     * @param id identificador de la encuesta a cerrar
     * @return Optional con la encuesta cerrada si se encontró
     */
    Optional<Survey> closeSurvey(String id);

    /**
     * Duplica una encuesta existente.
     *
     * @param id identificador de la encuesta a duplicar
     * @return Optional con la encuesta duplicada si se encontró
     */
    Optional<Survey> duplicateSurvey(String id);

    /**
     * Actualiza la fecha de programación de una encuesta.
     *
     * @param id identificador de la encuesta
     * @param scheduledOpen fecha de apertura programada (ej: "2024-03-23T10:00:00")
     * @param scheduledClose fecha de cierre programada (ej: "2024-03-23T18:00:00")
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> updateSchedule(String id, String scheduledOpen, String scheduledClose);

    /**
     * Actualiza la configuración de branding de una encuesta.
     *
     * @param id identificador de la encuesta
     * @param branding nueva configuración de branding
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> updateBranding(String id, Branding branding);

    /**
     * Agrega una pregunta a una encuesta existente.
     *
     * @param surveyId identificador de la encuesta
     * @param question la pregunta a agregar
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> addQuestion(String surveyId, Question question);

    /**
     * Elimina una pregunta de una encuesta existente.
     *
     * @param surveyId identificador de la encuesta
     * @param questionId identificador de la pregunta a eliminar
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> removeQuestion(String surveyId, String questionId);

    /**
     * Actualiza una pregunta existente en una encuesta.
     *
     * @param surveyId identificador de la encuesta
     * @param questionId identificador de la pregunta a actualizar
     * @param question los nuevos datos de la pregunta
     * @return Optional con la encuesta actualizada si se encontró
     */
    Optional<Survey> updateQuestion(String surveyId, String questionId, Question question);

    /**
     * Crea una nueva versión de una encuesta existente.
     *
     * @param surveyId identificador de la encuesta a duplicar
     * @return Optional con la encuesta duplicada si se encontró
     */
    Optional<Survey> createNewVersion(String surveyId);

    /**
     * Obtiene el historial de versiones de una encuesta.
     *
     * @param originalSurveyId identificador de la encuesta original
     * @return lista de encuestas que son versiones de la original
     */
    List<Survey> getSurveyVersionHistory(String originalSurveyId);
}
