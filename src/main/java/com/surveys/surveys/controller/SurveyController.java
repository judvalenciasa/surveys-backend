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

/**
 * Controlador REST que maneja las operaciones CRUD y acciones específicas para encuestas.
 * Proporciona endpoints para la gestión completa del ciclo de vida de las encuestas.
 *
 * <p>Los endpoints base incluyen:
 * <ul>
 *   <li>POST /api/surveys - Crear nueva encuesta</li>
 *   <li>GET /api/surveys - Listar encuestas</li>
 *   <li>GET /api/surveys/{id} - Obtener encuesta específica</li>
 *   <li>PUT /api/surveys/{id} - Actualizar encuesta</li>
 *   <li>DELETE /api/surveys/{id} - Eliminar encuesta</li>
 * </ul>
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

    /**
     * Crea una nueva encuesta.
     * El estado inicial de la encuesta se establece como CREADA.
     *
     * @param survey la encuesta a crear
     * @return ResponseEntity con la encuesta creada y código HTTP 201
     */
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        try {
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication.getPrincipal() instanceof User)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            
            User user = (User) authentication.getPrincipal();
            
            // Establecer los valores iniciales
            survey.setAdminId(user.getId());
            survey.setStatus(SurveyStatus.CREADA);
            survey.setCreatedAt(Instant.now());
            survey.setModifiedAt(Instant.now());
            survey.setVersion(1);
            survey.setTemplate(false);
            
            if (survey.getQuestions() == null) {
                survey.setQuestions(new ArrayList<>());
            }
            
            Survey savedSurvey = this.surveyService.saveSurvey(survey);
            return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene todas las encuestas con filtros opcionales.
     *
     * @param status filtro por estado de la encuesta
     * @param isTemplate filtro para obtener solo plantillas
     * @return ResponseEntity con la lista de encuestas que coinciden con los filtros
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
     *
     * @param id identificador de la encuesta
     * @return ResponseEntity con la encuesta si existe, o 404 si no se encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable String id) {
        return this.surveyService.getSurveyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza una encuesta existente.
     *
     * @param id identificador de la encuesta a actualizar
     * @param survey nueva información de la encuesta
     * @return ResponseEntity con la encuesta actualizada, o 404 si no se encuentra
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
     *
     * @param id identificador de la encuesta a eliminar
     * @return ResponseEntity vacío con 200 si se eliminó, o 404 si no se encuentra
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
     *
     * @param name nombre parcial de la encuesta (opcional)
     * @param adminId ID del administrador (opcional)
     * @return ResponseEntity con la lista de encuestas que coinciden con los criterios
     */
    @GetMapping("/search")
    public ResponseEntity<List<Survey>> searchSurveys(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String adminId) {
        List<Survey> surveys = this.surveyService.searchSurveys(name, adminId);
        return ResponseEntity.ok(surveys);
    }

    /**
     * Publica una encuesta, cambiando su estado a PUBLICADA.
     *
     * @param id identificador de la encuesta a publicar
     * @return ResponseEntity con la encuesta publicada, o 404 si no se encuentra
     */
    @PostMapping("/{id}/publish")
    public ResponseEntity<Survey> publishSurvey(@PathVariable String id) {
        return this.surveyService.publishSurvey(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cierra una encuesta, cambiando su estado a CERRADA.
     *
     * @param id identificador de la encuesta a cerrar
     * @return ResponseEntity con la encuesta cerrada, o 404 si no se encuentra
     */
    @PostMapping("/{id}/close")
    public ResponseEntity<Survey> closeSurvey(@PathVariable String id) {
        return this.surveyService.closeSurvey(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Duplica una encuesta existente.
     * Crea una nueva encuesta con los mismos datos pero diferente ID.
     *
     * @param id identificador de la encuesta a duplicar
     * @return ResponseEntity con la nueva encuesta y código 201, o 404 si no se encuentra la original
     */
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<Survey> duplicateSurvey(@PathVariable String id) {
        return this.surveyService.duplicateSurvey(id)
            .map(survey -> new ResponseEntity<>(survey, HttpStatus.CREATED))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las plantillas de encuestas.
     *
     * @return ResponseEntity con la lista de plantillas disponibles
     */
    @GetMapping("/templates")
    public ResponseEntity<List<Survey>> getAllTemplates() {
        List<Survey> templates = this.surveyService.getSurveys(null, true);
        return ResponseEntity.ok(templates);
    }

    /**
     * Actualiza la programación de una encuesta.
     *
     * @param id identificador de la encuesta
     * @param scheduledOpen fecha y hora de apertura en formato ISO-8601
     * @param scheduledClose fecha y hora de cierre en formato ISO-8601
     * @return ResponseEntity con la encuesta actualizada, o 404 si no se encuentra
     */
    @PatchMapping("/{id}/schedule")
    public ResponseEntity<Survey> updateSchedule(
            @PathVariable String id,
            @RequestParam String scheduledOpen,
            @RequestParam String scheduledClose) {
        return this.surveyService.updateSchedule(id, scheduledOpen, scheduledClose)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza la configuración visual de una encuesta.
     *
     * @param id identificador de la encuesta
     * @param branding nueva configuración visual
     * @return ResponseEntity con la encuesta actualizada, o 404 si no se encuentra
     */
    @PatchMapping("/{id}/branding")
    public ResponseEntity<Survey> updateBranding(
            @PathVariable String id,
            @RequestBody Branding branding) {
        return this.surveyService.updateBranding(id, branding)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
