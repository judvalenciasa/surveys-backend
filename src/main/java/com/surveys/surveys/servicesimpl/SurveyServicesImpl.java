package com.surveys.surveys.servicesimpl;

import com.surveys.surveys.model.Survey;
import com.surveys.surveys.repository.SurveyRepository;
import com.surveys.surveys.services.SurveyService;
import com.surveys.surveys.enums.SurveyStatus;
import com.surveys.surveys.model.Branding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.surveys.surveys.model.Question;
import java.util.ArrayList;
import java.time.Instant; // ✅ AÑADIR este import

/**
 * Implementación del servicio de encuestas con MongoDB.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Service
public class SurveyServicesImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

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
        Optional<Survey> surveyOpt = surveyRepository.findById(id);
        
        if (surveyOpt.isPresent()) {
            Survey survey = surveyOpt.get();
            // ✅ AÑADIR: Verificar y actualizar estado automáticamente
            Survey updatedSurvey = checkAndUpdateSurveyStatus(survey);
            return Optional.of(updatedSurvey);
        }
        
        return Optional.empty();
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
    public Optional<Survey> updateBranding(String id, Branding branding) {
        return surveyRepository.findById(id)
            .map(survey -> {
                survey.setBranding(branding);
                return surveyRepository.save(survey);
            });
    }

    @Override
    public Optional<Survey> addQuestion(String surveyId, Question question) {
        return surveyRepository.findById(surveyId)
            .map(survey -> {
                if (survey.getQuestions() == null) {
                    survey.setQuestions(new ArrayList<>());
                }
                
                question.setOrder(survey.getQuestions().size() + 1);
                survey.getQuestions().add(question);
                return surveyRepository.save(survey);
            });
    }

    @Override
    public Optional<Survey> removeQuestion(String surveyId, String questionId) {
        return surveyRepository.findById(surveyId)
            .map(survey -> {
                survey.setQuestions(
                    survey.getQuestions().stream()
                        .filter(q -> !q.getId().equals(questionId))
                        .collect(Collectors.toList())
                );
                return surveyRepository.save(survey);
            });
    }

    @Override
    public Optional<Survey> updateQuestion(String surveyId, String questionId, Question question) {
        return surveyRepository.findById(surveyId)
            .map(survey -> {
                List<Question> questions = survey.getQuestions();
                for (int i = 0; i < questions.size(); i++) {
                    if (questions.get(i).getId().equals(questionId)) {
                        question.setId(questionId);
                        question.setOrder(questions.get(i).getOrder());
                        questions.set(i, question);
                        break;
                    }
                }
                return surveyRepository.save(survey);
            });
    }

    @Override
    public Optional<Survey> createNewVersion(String surveyId) {
        return surveyRepository.findById(surveyId)
            .map(original -> {
                Survey newVersion = new Survey();
                newVersion.setName(original.getName() + " (Nueva Versión)");
                newVersion.setDescription(original.getDescription());
                newVersion.setBranding(original.getBranding());
                newVersion.setQuestions(new ArrayList<>(original.getQuestions()));
                newVersion.setPreviousVersionId(original.getId());
                newVersion.setAdminId(original.getAdminId());
                return surveyRepository.save(newVersion);
            });
    }

    @Override
    public List<Survey> getSurveyVersionHistory(String originalSurveyId) {
        List<Survey> versions = new ArrayList<>();
        String currentId = originalSurveyId;
        
        while (currentId != null) {
            Optional<Survey> currentVersion = surveyRepository.findById(currentId);
            if (currentVersion.isPresent()) {
                versions.add(currentVersion.get());
                currentId = currentVersion.get().getPreviousVersionId();
            } else {
                break;
            }
        }
        
        return versions;
    }

    /**
     * ✅ NUEVO: Verifica y actualiza automáticamente el estado basado en scheduledClose
     */
    @Override
    public Survey checkAndUpdateSurveyStatus(Survey survey) {
        if (survey == null) {
            return survey;
        }
        
        Instant now = Instant.now();
        
       
        if (survey.getStatus() == SurveyStatus.PUBLICADA && 
            survey.getScheduledClose() != null && 
            now.isAfter(survey.getScheduledClose())) {
            
            System.out.println("🔄 Auto-closing survey: " + survey.getId() + 
                             " (scheduled close: " + survey.getScheduledClose() + 
                             ", current time: " + now + ")");
            
        
            survey.setStatus(SurveyStatus.CERRADA);
            
            
            Survey savedSurvey = surveyRepository.save(survey);
            
            System.out.println(" Survey auto-closed successfully: " + survey.getId());
            
            return savedSurvey;
        }
        
        return survey;
    }
}
