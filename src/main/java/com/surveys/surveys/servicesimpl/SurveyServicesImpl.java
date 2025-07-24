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

/**
 * Implementación del servicio de gestión de encuestas.
 * Esta clase proporciona la implementación concreta de todas las operaciones
 * definidas en la interfaz {@link SurveyService}, utilizando MongoDB como
 * almacenamiento de datos.
 * 
 * <p>Las principales funcionalidades incluyen:
 * <ul>
 *   <li>Gestión del ciclo de vida completo de las encuestas</li>
 *   <li>Manejo de estados y programación</li>
 *   <li>Administración de preguntas</li>
 *   <li>Control de versiones</li>
 * </ul>
 *
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 * @see SurveyService
 * @see Survey
 */
@Service
public class SurveyServicesImpl implements SurveyService {

    /** Repositorio para realizar operaciones CRUD con las encuestas en MongoDB */
    @Autowired
    private SurveyRepository surveyRepository;


    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación valida que la encuesta no sea null antes de guardarla.
     *
     * @throws IllegalArgumentException si la encuesta es null
     */
    @Override
    public Survey saveSurvey(Survey survey) {
        if (survey == null) {
            throw new IllegalArgumentException("La encuesta no puede ser null");
        }
        Survey savedSurvey = surveyRepository.save(survey);
        return savedSurvey;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación permite filtrar por:
     * <ul>
     *   <li>Estado de la encuesta</li>
     *   <li>Si es una plantilla o no</li>
     * </ul>
     * Si no se especifica ningún filtro, retorna todas las encuestas.
     */
    @Override
    public List<Survey> getSurveys(SurveyStatus status, Boolean isTemplate) {
        if (status != null) {
            return surveyRepository.findByStatus(status);
        } else if (isTemplate != null) {
            return surveyRepository.findByIsTemplate(isTemplate);
        }
        return surveyRepository.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Survey> getSurveyById(String id) {
        return surveyRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación preserva la fecha de creación original
     * al actualizar una encuesta existente.
     */
    @Override
    public Optional<Survey> updateSurvey(String id, Survey survey) {
        return surveyRepository.findById(id)
            .map(existingSurvey -> {
                survey.setId(id);
                survey.setCreatedAt(existingSurvey.getCreatedAt());
                return surveyRepository.save(survey);
            });
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación verifica la existencia de la encuesta
     * antes de intentar eliminarla.
     *
     * @return true si la encuesta fue eliminada, false si no existía
     */
    @Override
    public boolean deleteSurvey(String id) {
        if (surveyRepository.existsById(id)) {
            surveyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación permite buscar por:
     * <ul>
     *   <li>Nombre de la encuesta (búsqueda parcial insensible a mayúsculas)</li>
     *   <li>ID del administrador</li>
     *   <li>Combinación de ambos criterios</li>
     * </ul>
     */
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

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación actualiza el estado de una encuesta
     * manteniendo el resto de sus propiedades intactas.
     */
    @Override
    public Optional<Survey> updateSurveyStatus(String id, SurveyStatus status) {
        return surveyRepository.findById(id)
            .map(survey -> {
                survey.setStatus(status);
                return surveyRepository.save(survey);
            });
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Esta implementación cambia el estado a {@link SurveyStatus#PUBLICADA}.
     */
    @Override
    public Optional<Survey> publishSurvey(String id) {
        return updateSurveyStatus(id, SurveyStatus.PUBLICADA);
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Esta implementación cambia el estado a {@link SurveyStatus#CERRADA}.
     */
    @Override
    public Optional<Survey> closeSurvey(String id) {
        return updateSurveyStatus(id, SurveyStatus.CERRADA);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación crea una copia exacta de la encuesta original,
     * agregando " (Copia)" al nombre para distinguirla.
     */
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


    /** {@inheritDoc} */
    @Override
    public Optional<Survey> updateBranding(String id, Branding branding) {
        return surveyRepository.findById(id)
            .map(survey -> {
                survey.setBranding(branding);
                return surveyRepository.save(survey);
            });
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación asigna automáticamente un orden
     * a la nueva pregunta basado en la posición al final de la lista.
     */
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

    /** {@inheritDoc} */
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

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación mantiene el orden original de la pregunta
     * al actualizarla.
     */
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

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación crea una nueva versión manteniendo:
     * <ul>
     *   <li>Todas las preguntas</li>
     *   <li>La configuración de branding</li>
     *   <li>La referencia a la versión anterior</li>
     * </ul>
     */
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

    /**
     * {@inheritDoc}
     * 
     * <p>Esta implementación recorre recursivamente la cadena de versiones
     * siguiendo los IDs de versiones anteriores hasta llegar a la primera versión.
     */
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

  
}
