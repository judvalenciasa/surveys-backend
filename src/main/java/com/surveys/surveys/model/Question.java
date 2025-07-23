package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa una pregunta dentro de una encuesta.
 * Esta clase define la estructura y propiedades de las preguntas
 * que pueden ser incluidas en una encuesta.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Document
public class Question {
    @Id
    private String id;
    private String text;
    private String type;        // MULTIPLE_CHOICE, TEXT, RATING, etc.
    private boolean required;
    private Object options;     // Puede ser una lista de opciones para preguntas de opción múltiple
    private Integer order;      // Orden de la pregunta en la encuesta
    
    // Constructor por defecto
    public Question() {
    }
    
    // Constructor con parámetros principales
    public Question(String text, String type, boolean required) {
        this.text = text;
        this.type = type;
        this.required = required;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    public Object getOptions() {
        return options;
    }
    
    public void setOptions(Object options) {
        this.options = options;
    }
    
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
}
