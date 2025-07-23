package com.surveys.surveys.servicesimpl;

import com.surveys.surveys.exception.ResourceNotFoundException;
import com.surveys.surveys.model.Question;
import com.surveys.surveys.repository.QuestionRepository;
import com.surveys.surveys.services.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(String id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question updateQuestion(String id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con id: " + id));

        question.setText(questionDetails.getText());
        question.setType(questionDetails.getType());
        question.setRequired(questionDetails.isRequired());
        question.setOptions(questionDetails.getOptions());
        question.setOrder(questionDetails.getOrder());

        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con id: " + id));
        questionRepository.delete(question);
    }

    @Override
    public List<Question> getQuestionsByType(String type) {
        return questionRepository.findByType(type);
    }

    @Override
    public List<Question> getQuestionsOrdered() {
        return questionRepository.findByOrderByOrderAsc();
    }
}
