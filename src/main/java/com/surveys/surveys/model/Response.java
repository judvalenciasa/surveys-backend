package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa una respuesta a una encuesta en el sistema.
 * Esta clase es la entidad principal que contiene toda la información
 * relacionada con las respuestas proporcionadas por los usuarios a una encuesta,
 * incluyendo la fecha de envío y las respuestas individuales a cada pregunta.
 *
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
     * Clase interna que representa una respuesta individual a una pregunta.
     * El campo answer es de tipo Object para permitir diferentes tipos de respuestas
     * (texto, números, archivos, etc.).
     */
    public static class Answer {
        private String questionId;
        private Object answer;

        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }

        public Object getAnswer() { return answer; }
        public void setAnswer(Object answer) { this.answer = answer; }
    }

    /**
     * Constructor por defecto.
     * Inicializa submittedAt con la fecha y hora actual.
     */
    public Response() {
        this.submittedAt = Instant.now();
    }

    // Getters y Setters
    /**
     * Obtiene el identificador único de la respuesta.
     * @return el ID de la respuesta
     */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Obtiene el ID de la encuesta a la que pertenece esta respuesta.
     * @return el ID de la encuesta
     */
    public String getSurveyId() { return surveyId; }
    public void setSurveyId(String surveyId) { this.surveyId = surveyId; }

    /**
     * Obtiene la fecha y hora en que se envió la respuesta.
     * @return el momento de envío
     */
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }

    /**
     * Obtiene la lista de respuestas individuales.
     * @return lista de respuestas a las preguntas
     */
    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
}
