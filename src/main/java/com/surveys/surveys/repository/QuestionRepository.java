package com.surveys.surveys.repository;

import com.surveys.surveys.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByOrderByOrderAsc();
    List<Question> findByType(String type);
}
