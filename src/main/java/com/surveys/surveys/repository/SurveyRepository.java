package com.surveys.surveys.repository;

import com.surveys.surveys.enums.SurveyStatus;
import com.surveys.surveys.model.Survey;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestión de encuestas con consultas personalizadas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {

    /**
     * Busca encuestas por nombre (case insensitive).
     */
    List<Survey> findByNameContainingIgnoreCase(String name);

    /**
     * Busca encuestas por administrador.
     */
    List<Survey> findByAdminId(String adminId);

    /**
     * Busca encuestas por estado.
     */
    List<Survey> findByStatus(SurveyStatus status);

    /**
     * Filtra encuestas por tipo (plantilla o normal).
     */
    List<Survey> findByIsTemplate(boolean isTemplate);

    /**
     * Busca encuestas por estado y administrador.
     */
    List<Survey> findByStatusAndAdminId(SurveyStatus status, String adminId);
}
