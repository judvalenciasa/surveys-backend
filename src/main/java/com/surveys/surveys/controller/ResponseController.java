package com.surveys.surveys.controller;

import com.surveys.surveys.model.Response;
import com.surveys.surveys.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de respuestas a encuestas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-01-27
 * @see ResponseService
 * @see Response
 */
@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    // ========================================
    // RUTAS PÚBLICAS
    // ========================================

    /**
     * Envía una respuesta a una encuesta publicada.
     * 
     * @param response respuesta de la encuesta
     * @return ResponseEntity con la respuesta guardada
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitResponse(@Valid @RequestBody Response response) {
        try {
            // Validaciones específicas
            if (response.getSurveyId() == null || response.getSurveyId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error", "SURVEY_ID_REQUIRED",
                        "message", "El ID de la encuesta es requerido",
                        "timestamp", Instant.now()
                    ));
            }
            
            if (response.getAnswers() == null || response.getAnswers().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error", "ANSWERS_REQUIRED", 
                        "message", "Se requiere al menos una respuesta",
                        "timestamp", Instant.now()
                    ));
            }
            
            // Validar estructura de respuestas
            for (int i = 0; i < response.getAnswers().size(); i++) {
                Response.Answer answer = response.getAnswers().get(i);
                if (answer.getQuestionId() == null || answer.getQuestionId().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                        .body(Map.of(
                            "error", "INVALID_QUESTION_ID",
                            "message", "La respuesta en posición " + i + " no tiene questionId válido",
                            "timestamp", Instant.now()
                        ));
                }
                if (answer.getAnswer() == null) {
                    return ResponseEntity.badRequest()
                        .body(Map.of(
                            "error", "EMPTY_ANSWER",
                            "message", "La respuesta para la pregunta " + answer.getQuestionId() + " está vacía",
                            "timestamp", Instant.now()
                        ));
                }
            }
            
            Response savedResponse = responseService.saveResponse(response);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "success", true,
                    "message", "Respuesta guardada exitosamente",
                    "responseId", savedResponse.getId(),
                    "surveyId", savedResponse.getSurveyId(),
                    "submittedAt", savedResponse.getSubmittedAt(),
                    "answerCount", savedResponse.getAnswers().size()
                ));
                
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "VALIDATION_ERROR",
                    "message", e.getMessage(),
                    "timestamp", Instant.now()
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "INTERNAL_ERROR",
                    "message", "Error interno del servidor",
                    "timestamp", Instant.now()
                ));
        }
    }

    // ========================================
    // RUTAS DE ADMINISTRACIÓN (Solo ADMIN)
    // ========================================

    /**
     * Obtiene todas las respuestas del sistema.
     * 
     * @return ResponseEntity con la lista de todas las respuestas
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<List<Response>> getAllResponses() {
        List<Response> responses = responseService.getAllResponses();
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene todas las respuestas de una encuesta específica.
     * 
     * @param surveyId identificador de la encuesta
     * @return ResponseEntity con la lista de respuestas de la encuesta
     */
    @GetMapping("/survey/{surveyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Response>> getResponsesBySurvey(@PathVariable String surveyId) {
        List<Response> responses = responseService.getResponsesBySurvey(surveyId);
        return ResponseEntity.ok(responses);
    }

}
