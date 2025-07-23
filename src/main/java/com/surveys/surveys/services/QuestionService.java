package com.surveys.surveys.services;

import com.surveys.surveys.model.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Question createQuestion(Question question);
    List<Question> getAllQuestions();
    Optional<Question> getQuestionById(String id);
    Question updateQuestion(String id, Question question);
    void deleteQuestion(String id);
    List<Question> getQuestionsByType(String type);
    List<Question> getQuestionsOrdered();
}
