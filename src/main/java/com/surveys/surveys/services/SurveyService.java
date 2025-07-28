package com.surveys.surveys.services;

import com.surveys.surveys.model.Survey;
import com.surveys.surveys.enums.SurveyStatus;
import com.surveys.surveys.model.Branding;
import com.surveys.surveys.model.Question;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión completa de encuestas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface SurveyService {
    
    /**
     * Guarda encuesta nueva o actualiza existente.
     */
    Survey saveSurvey(Survey survey);

    /**
     * Obtiene encuestas filtradas por estado y tipo.
     */
    List<Survey> getSurveys(SurveyStatus status, Boolean isTemplate);

    /**
     * Busca encuesta por ID.
     */
    Optional<Survey> getSurveyById(String id);

    /**
     * Actualiza datos de encuesta existente.
     */
    Optional<Survey> updateSurvey(String id, Survey survey);

    /**
     * Elimina encuesta por ID.
     */
    boolean deleteSurvey(String id);

    /**
     * Busca encuestas por nombre y administrador.
     */
    List<Survey> searchSurveys(String name, String adminId);

    /**
     * Actualiza estado de encuesta.
     */
    Optional<Survey> updateSurveyStatus(String id, SurveyStatus status);

    /**
     * Publica encuesta (cambia estado a PUBLICADA).
     */
    Optional<Survey> publishSurvey(String id);

    /**
     * Cierra encuesta (cambia estado a CERRADA).
     */
    Optional<Survey> closeSurvey(String id);

    /**
     * Duplica encuesta existente.
     */
    Optional<Survey> duplicateSurvey(String id);

    /**
     * Actualiza configuración de branding.
     */
    Optional<Survey> updateBranding(String id, Branding branding);

    /**
     * Agrega pregunta a encuesta.
     */
    Optional<Survey> addQuestion(String surveyId, Question question);

    /**
     * Elimina pregunta de encuesta.
     */
    Optional<Survey> removeQuestion(String surveyId, String questionId);

    /**
     * Actualiza pregunta existente.
     */
    Optional<Survey> updateQuestion(String surveyId, String questionId, Question question);

    /**
     * Crea nueva versión de encuesta.
     */
    Optional<Survey> createNewVersion(String surveyId);

    /**
     * Obtiene historial de versiones.
     */
    List<Survey> getSurveyVersionHistory(String originalSurveyId);

    /**
     * Verifica y actualiza el estado de una encuesta.
     */
    Survey checkAndUpdateSurveyStatus(Survey survey);
}
