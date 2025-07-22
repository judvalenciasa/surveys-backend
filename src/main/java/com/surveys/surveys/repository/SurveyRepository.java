package com.surveys.surveys.repository;

import com.surveys.surveys.model.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReadPreference;

@ReadPreference
public interface SurveyRepository extends MongoRepository<Survey, String> {
}
