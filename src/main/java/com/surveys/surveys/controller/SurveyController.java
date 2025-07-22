package com.surveys.surveys.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.surveys.surveys.model.Survey;
import com.surveys.surveys.model.SurveyStatus;
import com.surveys.surveys.model.Branding;
import com.surveys.surveys.service.surveyService;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
    @Autowired
    private surveyService surveyService;

    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        // Aseguramos que una nueva encuesta siempre empiece como CREADA
        survey.setStatus(SurveyStatus.CREADA);
        Survey savedSurvey = this.surveyService.saveSurvey(survey);
        return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Survey>> getAllSurveys(
            @RequestParam(required = false) SurveyStatus status,
            @RequestParam(required = false) Boolean isTemplate) {
        List<Survey> surveys = this.surveyService.getSurveys(status, isTemplate);
        return ResponseEntity.ok(surveys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable String id) {
        return this.surveyService.getSurveyById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(
            @PathVariable String id, 
            @RequestBody Survey survey) {
        return this.surveyService.updateSurvey(id, survey)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable String id) {
        if (this.surveyService.deleteSurvey(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Survey>> searchSurveys(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String adminId) {
        List<Survey> surveys = this.surveyService.searchSurveys(name, adminId);
        return ResponseEntity.ok(surveys);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Survey> publishSurvey(@PathVariable String id) {
        return this.surveyService.publishSurvey(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Survey> closeSurvey(@PathVariable String id) {
        return this.surveyService.closeSurvey(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<Survey> duplicateSurvey(@PathVariable String id) {
        return this.surveyService.duplicateSurvey(id)
            .map(survey -> new ResponseEntity<>(survey, HttpStatus.CREATED))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/templates")
    public ResponseEntity<List<Survey>> getAllTemplates() {
        List<Survey> templates = this.surveyService.getSurveys(null, true);
        return ResponseEntity.ok(templates);
    }

    @PatchMapping("/{id}/schedule")
    public ResponseEntity<Survey> updateSchedule(
            @PathVariable String id,
            @RequestParam String scheduledOpen,
            @RequestParam String scheduledClose) {
        return this.surveyService.updateSchedule(id, scheduledOpen, scheduledClose)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/branding")
    public ResponseEntity<Survey> updateBranding(
            @PathVariable String id,
            @RequestBody Branding branding) {
        return this.surveyService.updateBranding(id, branding)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
