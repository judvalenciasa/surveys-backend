package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

/**
 * Respuesta de un usuario a una encuesta.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Document(collection = "responses")
public class Response {
    @Id
    private String id;
    private String surveyId;
    private Instant submittedAt;
    private List<Answer> answers = new ArrayList<>();

    /**
     * Respuesta individual a una pregunta.
     */
    public static class Answer {
        private String questionId;
        private Object answer;

        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }

        public Object getAnswer() { return answer; }
        public void setAnswer(Object answer) { this.answer = answer; }
    }

    public Response() {
        this.submittedAt = Instant.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSurveyId() { return surveyId; }
    public void setSurveyId(String surveyId) { this.surveyId = surveyId; }

    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }

    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
}
