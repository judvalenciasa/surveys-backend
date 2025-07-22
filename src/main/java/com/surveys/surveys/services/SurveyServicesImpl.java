package com.surveys.surveys.services;

import com.surveys.surveys.model.Survey;
import com.surveys.surveys.model.SurveyStatus;
import com.surveys.surveys.repository.SurveyRepository;
import com.surveys.surveys.model.Branding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de {@link SurveyService} que proporciona
 * la lógica de negocio para la gestión de encuestas utilizando
 * MongoDB como almacenamiento de datos.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 * @see SurveyService
 * @see Survey
 */
@Service
public class SurveyServicesImpl implements SurveyService {

    /** Repositorio para operaciones con la base de datos */
    @Autowired
    private SurveyRepository surveyRepository;

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException si la encuesta es null
     */
    @Override
    public Survey saveSurvey(Survey survey) {
        if (survey == null) {
            throw new IllegalArgumentException("La encuesta no puede ser null");
        }
        return surveyRepository.save(survey);
    }

    @Override
    public List<Survey> getSurveys(SurveyStatus status, Boolean isTemplate) {
        if (status != null) {
            return surveyRepository.findByStatus(status);
        } else if (isTemplate != null) {
            return surveyRepository.findByIsTemplate(isTemplate);
        }
        return surveyRepository.findAll();
    }

    @Override
    public Optional<Survey> getSurveyById(String id) {
        return surveyRepository.findById(id);
    }

    @Override
    public Optional<Survey> updateSurvey(String id, Survey survey) {
        return surveyRepository.findById(id)
            .map(existingSurvey -> {
                survey.setId(id);
                survey.setCreatedAt(existingSurvey.getCreatedAt());
                return surveyRepository.save(survey);
            });
    }

    @Override
    public boolean deleteSurvey(String id) {
        if (surveyRepository.existsById(id)) {
            surveyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Survey> searchSurveys(String name, String adminId) {
        if (name != null && adminId != null) {
            return surveyRepository.findByNameContainingIgnoreCase(name).stream()
                .filter(s -> s.getAdminId().equals(adminId))
                .collect(Collectors.toList());
        } else if (name != null) {
            return surveyRepository.findByNameContainingIgnoreCase(name);
        } else if (adminId != null) {
            return surveyRepository.findByAdminId(adminId);
        }
        return surveyRepository.findAll();
    }

    @Override
    public Optional<Survey> updateSurveyStatus(String id, SurveyStatus status) {
        return surveyRepository.findById(id)
            .map(survey -> {
                survey.setStatus(status);
                return surveyRepository.save(survey);
            });
    }

    @Override
    public Optional<Survey> publishSurvey(String id) {
        return updateSurveyStatus(id, SurveyStatus.PUBLICADA);
    }

    @Override
    public Optional<Survey> closeSurvey(String id) {
        return updateSurveyStatus(id, SurveyStatus.CERRADA);
    }

    @Override
    public Optional<Survey> duplicateSurvey(String id) {
        return surveyRepository.findById(id)
            .map(original -> {
                Survey copy = new Survey();
                copy.setName(original.getName() + " (Copia)");
                copy.setDescription(original.getDescription());
                copy.setBranding(original.getBranding());
                copy.setAdminId(original.getAdminId());
                return surveyRepository.save(copy);
            });
    }

    @Override
    public Optional<Survey> updateSchedule(String id, String scheduledOpen, String scheduledClose) {
        return surveyRepository.findById(id)
            .map(survey -> {
                survey.setScheduledOpen(Instant.parse(scheduledOpen));
                survey.setScheduledClose(Instant.parse(scheduledClose));
                return surveyRepository.save(survey);
            });
    }

    @Override
    public Optional<Survey> updateBranding(String id, Branding branding) {
        return surveyRepository.findById(id)
            .map(survey -> {
                survey.setBranding(branding);
                return surveyRepository.save(survey);
            });
    }
}
