package com.surveys.surveys.servicesimpl;

import com.surveys.surveys.model.Response;
import com.surveys.surveys.repository.ResponseRepository;
import com.surveys.surveys.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de respuestas que proporciona
 * la lógica de negocio para la gestión de respuestas utilizando
 * MongoDB como almacenamiento de datos.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 * @see ResponseService
 * @see Response
 */
@Service
public class ResponseServiceImpl implements ResponseService {
    
    /** Repositorio para operaciones con la base de datos */
    @Autowired
    private ResponseRepository responseRepository;
    
    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException si la respuesta es null
     */
    @Override
    public Response saveResponse(Response response) {
        if (response == null) {
            throw new IllegalArgumentException("La respuesta no puede ser null");
        }
        return responseRepository.save(response);
    }
    
    /** {@inheritDoc} */
    @Override
    public Optional<Response> getResponseById(String id) {
        return responseRepository.findById(id);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }
    
    /** {@inheritDoc} */
    @Override
    public void deleteResponse(String id) {
        responseRepository.deleteById(id);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Response> getResponsesBySurvey(String surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Response> getResponsesByDateRange(Instant startDate, Instant endDate) {
        return responseRepository.findBySubmittedAtBetween(startDate, endDate);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Response> getResponsesBySurveyAndDateRange(
            String surveyId, Instant startDate, Instant endDate) {
        return responseRepository.findBySurveyIdAndSubmittedAtBetween(
            surveyId, startDate, endDate);
    }
    
    /** {@inheritDoc} */
    @Override
    public long getResponseCount(String surveyId) {
        return responseRepository.countBySurveyId(surveyId);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Response> getLatestResponses(String surveyId, int limit) {
        return responseRepository.findTopBySurveyId(surveyId);
    }
}
