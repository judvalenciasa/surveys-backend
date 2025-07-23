package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa una encuesta en el sistema.
 * Esta clase es la entidad principal que contiene toda la información
 * relacionada con una encuesta, incluyendo su configuración visual,
 * estado y programación.
 *
 * <p>Ejemplo de uso:
 * <pre>
 * Survey survey = new Survey();
 * survey.setName("Encuesta de Satisfacción");
 * survey.setStatus(SurveyStatus.CREADA);
 * </pre>
 *
 * @author TuNombre
 * @version 1.0
 * @since 2024-03-22
 */
@Document(collection = "surveys")
public class Survey {
    @Id
    private String id;
    private String name;
    private String description;
    private Integer version;
    private SurveyStatus status;
    private Instant createdAt;
    private Instant scheduledOpen;
    private Instant scheduledClose;
    private boolean isTemplate;
    private String adminId;
    private Branding branding;
    private String previousVersionId;
    private List<Question> questions;

    public Survey() {
        this.createdAt = Instant.now();
        this.version = 1;
        this.isTemplate = false;
        this.status = SurveyStatus.CREADA;
        this.questions = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public SurveyStatus getStatus() { return status; }
    public void setStatus(SurveyStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getScheduledOpen() { return scheduledOpen; }
    public void setScheduledOpen(Instant scheduledOpen) { this.scheduledOpen = scheduledOpen; }

    public Instant getScheduledClose() { return scheduledClose; }
    public void setScheduledClose(Instant scheduledClose) { this.scheduledClose = scheduledClose; }

    public boolean isTemplate() { return isTemplate; }
    public void setTemplate(boolean template) { isTemplate = template; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public Branding getBranding() { return branding; }
    public void setBranding(Branding branding) { this.branding = branding; }

    public String getPreviousVersionId() { return previousVersionId; }
    public void setPreviousVersionId(String previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
