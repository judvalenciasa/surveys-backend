package com.surveys.surveys.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.surveys.surveys.model.Survey;
import com.surveys.surveys.enums.SurveyStatus;
import com.surveys.surveys.model.Branding;
import com.surveys.surveys.services.SurveyService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.surveys.surveys.model.User;
import java.time.Instant;
import java.util.ArrayList;
import com.surveys.surveys.model.Question;

/**
 * Controlador REST para la gestión completa de encuestas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    // ========================================
    // RUTAS PÚBLICAS
    // ========================================

    /**
     * Obtiene encuestas publicadas disponibles para responder.
     */
    @GetMapping("/published")
    public ResponseEntity<List<Survey>> getPublishedSurveys() {
        List<Survey> surveys = surveyService.getSurveys(SurveyStatus.PUBLICADA, false);
        return ResponseEntity.ok(surveys);
    }

    /**
     * Obtiene información pública de una encuesta específica.
     */
    @GetMapping("/{id}/view")
    public ResponseEntity<Survey> viewSurvey(@PathVariable String id) {
        return surveyService.getSurveyById(id)
                .filter(survey -> survey.getStatus() == SurveyStatus.PUBLICADA)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========================================
    // RUTAS DE ADMINISTRACIÓN (Solo ADMIN)
    // ========================================

    /**
     * Crea una nueva encuesta.
     */
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Establecer valores iniciales
        survey.setAdminId(user.getId());
        survey.setStatus(SurveyStatus.CREADA);
        survey.setCreatedAt(Instant.now());
        survey.setModifiedAt(Instant.now());
        survey.setVersion(1);
        survey.setTemplate(false);

        if (survey.getQuestions() == null) {
            survey.setQuestions(new ArrayList<>());
        } else {
            for (Question question : survey.getQuestions()) {
                if (question.getId() == null || question.getId().isEmpty()) {
                    question.setId(java.util.UUID.randomUUID().toString());
                }
            }
        }

        Survey savedSurvey = this.surveyService.saveSurvey(survey);
        return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las encuestas con filtros opcionales.
     */
    @GetMapping
    public ResponseEntity<List<Survey>> getAllSurveys(
            @RequestParam(required = false) SurveyStatus status,
            @RequestParam(required = false) Boolean isTemplate) {
        List<Survey> surveys = this.surveyService.getSurveys(status, isTemplate);
        return ResponseEntity.ok(surveys);
    }

    /**
     * Obtiene una encuesta por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable String id) {
        return this.surveyService.getSurveyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza una encuesta existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(
            @PathVariable String id,
            @RequestBody Survey survey) {
        return this.surveyService.updateSurvey(id, survey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una encuesta.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable String id) {
        if (this.surveyService.deleteSurvey(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Busca encuestas por nombre y/o adminId.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Survey>> searchSurveys(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String adminId) {
        List<Survey> surveys = this.surveyService.searchSurveys(name, adminId);
        return ResponseEntity.ok(surveys);
    }

    /**
     * Publica una encuesta.
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<Survey> publishSurvey(@PathVariable String id) {
        return this.surveyService.publishSurvey(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cierra una encuesta.
     */
    @PostMapping("/{id}/close")
    public ResponseEntity<Survey> closeSurvey(@PathVariable String id) {
        return this.surveyService.closeSurvey(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Duplica una encuesta existente.
     */
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<Survey> duplicateSurvey(@PathVariable String id) {
        return this.surveyService.duplicateSurvey(id)
                .map(survey -> new ResponseEntity<>(survey, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las plantillas de encuestas.
     */
    @GetMapping("/templates")
    public ResponseEntity<List<Survey>> getAllTemplates() {
        List<Survey> templates = this.surveyService.getSurveys(null, true);
        return ResponseEntity.ok(templates);
    }

    /**
     * Actualiza la configuración visual de una encuesta.
     */
    @PatchMapping("/{id}/branding")
    public ResponseEntity<Survey> updateBranding(
            @PathVariable String id,
            @RequestBody Branding branding) {
        return this.surveyService.updateBranding(id, branding)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva versión de una encuesta.
     */
    @PostMapping("/{id}/version")
    public ResponseEntity<Survey> createNewVersion(@PathVariable String id) {
        return surveyService.createNewVersion(id)
                .map(survey -> new ResponseEntity<>(survey, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene el historial de versiones de una encuesta.
     */
    @GetMapping("/{id}/versions")
    public ResponseEntity<List<Survey>> getSurveyVersionHistory(@PathVariable String id) {
        List<Survey> versions = surveyService.getSurveyVersionHistory(id);
        return ResponseEntity.ok(versions);
    }

    // ========================================
    // GESTIÓN DE PREGUNTAS (Solo ADMIN)
    // ========================================

    /**
     * Agrega una nueva pregunta a una encuesta.
     */
    @PostMapping("/{surveyId}/questions")
    public ResponseEntity<Survey> addQuestion(
            @PathVariable String surveyId,
            @RequestBody Question question) {
        if (question.getId() == null || question.getId().isEmpty()) {
            question.setId(java.util.UUID.randomUUID().toString());
        }

        return surveyService.addQuestion(surveyId, question)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las preguntas de una encuesta.
     */
    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable String surveyId) {
        return surveyService.getSurveyById(surveyId)
                .map(survey -> ResponseEntity.ok(survey.getQuestions()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene una pregunta específica de una encuesta.
     */
    @GetMapping("/{surveyId}/questions/{questionId}")
    public ResponseEntity<Question> getQuestionById(
            @PathVariable String surveyId,
            @PathVariable String questionId) {
        return surveyService.getSurveyById(surveyId)
                .map(survey -> survey.getQuestions().stream()
                        .filter(q -> q.getId().equals(questionId))
                        .findFirst()
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza una pregunta existente en una encuesta.
     */
    @PutMapping("/{surveyId}/questions/{questionId}")
    public ResponseEntity<Survey> updateQuestion(
            @PathVariable String surveyId,
            @PathVariable String questionId,
            @RequestBody Question question) {
        return surveyService.updateQuestion(surveyId, questionId, question)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una pregunta de una encuesta.
     */
    @DeleteMapping("/{surveyId}/questions/{questionId}")
    public ResponseEntity<Void> removeQuestion(
            @PathVariable String surveyId,
            @PathVariable String questionId) {
        return surveyService.removeQuestion(surveyId, questionId)
                .map(s -> ResponseEntity.ok().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

}
