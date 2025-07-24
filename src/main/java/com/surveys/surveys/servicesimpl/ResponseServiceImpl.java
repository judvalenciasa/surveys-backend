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
 * Implementación del servicio de respuestas con MongoDB.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Service
public class ResponseServiceImpl implements ResponseService {
    
    @Autowired
    private ResponseRepository responseRepository;
    
    @Override
    public Response saveResponse(Response response) {
        if (response == null) {
            throw new IllegalArgumentException("La respuesta no puede ser null");
        }
        return responseRepository.save(response);
    }
    
    @Override
    public Optional<Response> getResponseById(String id) {
        return responseRepository.findById(id);
    }
    
    @Override
    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }
    
    @Override
    public void deleteResponse(String id) {
        responseRepository.deleteById(id);
    }
    
    @Override
    public List<Response> getResponsesBySurvey(String surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }
    
    @Override
    public List<Response> getResponsesByDateRange(Instant startDate, Instant endDate) {
        return responseRepository.findBySubmittedAtBetween(startDate, endDate);
    }
    
    @Override
    public List<Response> getResponsesBySurveyAndDateRange(
            String surveyId, Instant startDate, Instant endDate) {
        return responseRepository.findBySurveyIdAndSubmittedAtBetween(
            surveyId, startDate, endDate);
    }
    
    @Override
    public long getResponseCount(String surveyId) {
        return responseRepository.countBySurveyId(surveyId);
    }
    
    @Override
    public List<Response> getLatestResponses(String surveyId, int limit) {
        return responseRepository.findTopBySurveyId(surveyId);
    }
}
