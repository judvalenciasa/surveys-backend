package com.surveys.surveys.controller;

import com.surveys.surveys.model.Response;
import com.surveys.surveys.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de respuestas a encuestas.
 * 
 * <p>
 * Este controlador proporciona endpoints para que los administradores
 * puedan gestionar y analizar las respuestas enviadas por los usuarios
 * a las encuestas publicadas.
 * 
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

    /**
     * Servicio para la lógica de negocio de respuestas.
     */
    @Autowired
    private ResponseService responseService;

    /**
     * Obtiene todas las respuestas del sistema.
     * 
     * <p>
     * Este endpoint permite a los administradores ver todas las respuestas
     * enviadas en el sistema, útil para análisis generales y auditoría.
     *
     * @return ResponseEntity con la lista de todas las respuestas
     */
    @GetMapping
    public ResponseEntity<List<Response>> getAllResponses() {
        List<Response> responses = responseService.getAllResponses();
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene todas las respuestas de una encuesta específica.
     * 
     * <p>
     * Este endpoint es fundamental para el análisis de resultados de una
     * encuesta, permitiendo a los administradores revisar todas las respuestas
     * recibidas para generar reportes y estadísticas.
     *
     * @param surveyId identificador de la encuesta
     * @return ResponseEntity con la lista de respuestas de la encuesta
     */
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<Response>> getResponsesBySurvey(@PathVariable String surveyId) {
        List<Response> responses = responseService.getResponsesBySurvey(surveyId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Endpoint público para enviar respuestas sin autenticación.
     */
    @PostMapping("/public/submit")
    public ResponseEntity<Response> submitPublicResponse(@Valid @RequestBody Response response) {
        try {
            Response savedResponse = responseService.saveResponse(response);
            return new ResponseEntity<>(savedResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

     /**
     * Método alternativo más específico para guardar respuestas con mejor documentación.
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

}
