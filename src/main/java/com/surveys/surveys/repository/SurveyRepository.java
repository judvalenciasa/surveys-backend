package com.surveys.surveys.repository;

import com.surveys.surveys.enums.SurveyStatus;
import com.surveys.surveys.model.Survey;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de encuestas en MongoDB.
 * Proporciona operaciones CRUD y consultas personalizadas para el manejo de encuestas.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Búsqueda por nombre (case insensitive)</li>
 *   <li>Filtrado por estado de encuesta</li>
 *   <li>Gestión de plantillas</li>
 *   <li>Búsqueda por administrador</li>
 *   <li>Consultas por fechas de programación</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {

    /**
     * Busca encuestas que contengan el texto especificado en el nombre.
     * La búsqueda es insensible a mayúsculas y minúsculas.
     *
     * @param name texto a buscar en el nombre
     * @return lista de encuestas que coinciden con el criterio
     */
    List<Survey> findByNameContainingIgnoreCase(String name);

    /**
     * Busca encuestas asociadas a un administrador específico.
     *
     * @param adminId ID del administrador
     * @return lista de encuestas del administrador
     */
    List<Survey> findByAdminId(String adminId);

    /**
     * Busca encuestas por su estado actual.
     *
     * @param status estado de la encuesta (CREADA, PUBLICADA, CERRADA)
     * @return lista de encuestas en el estado especificado
     */
    List<Survey> findByStatus(SurveyStatus status);

    /**
     * Filtra encuestas según si son plantillas o no.
     *
     * @param isTemplate true para obtener plantillas, false para encuestas normales
     * @return lista de encuestas según el criterio de plantilla
     */
    List<Survey> findByIsTemplate(boolean isTemplate);

    /**
     * Busca encuestas por estado y administrador.
     *
     * @param status estado de la encuesta
     * @param adminId ID del administrador
     * @return lista de encuestas que cumplen ambos criterios
     */
    List<Survey> findByStatusAndAdminId(SurveyStatus status, String adminId);

}
