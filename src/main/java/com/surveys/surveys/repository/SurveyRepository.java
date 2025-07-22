package com.surveys.surveys.repository;

import com.surveys.surveys.model.Survey;
import com.surveys.surveys.model.SurveyStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SurveyRepository extends MongoRepository<Survey, String> {
    List<Survey> findByNameContainingIgnoreCase(String name);
    List<Survey> findByAdminId(String adminId);
    List<Survey> findByStatus(SurveyStatus status);
    List<Survey> findByIsTemplate(boolean isTemplate);
}
