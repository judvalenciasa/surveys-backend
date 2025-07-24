package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Pregunta de una encuesta con validaciones.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Document
public class Question {
    @Id
    private String id;

    @NotBlank(message = "El texto de la pregunta es obligatorio")
    @Size(min = 2, max = 500, message = "El texto debe tener entre 5 y 500 caracteres")
    private String text;

    @NotNull(message = "El tipo de pregunta es obligatorio")
    private String type; 
    
    @NotNull(message = "El campo required no puede ser null")
    private boolean required;
    
    private Object options;
    
    @Min(value = 1, message = "El orden debe ser mayor a 0")
    private Integer order;      
    
    public Question() {
        this.required = false;
    }
    
    public Question(String text, String type, boolean required) {
        this.text = text;
        this.type = type;
        this.required = required;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    
    public Object getOptions() { return options; }
    public void setOptions(Object options) { this.options = options; }
    
    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }
}
