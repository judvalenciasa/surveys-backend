package com.surveys.surveys.repository;

import com.surveys.surveys.enums.SurveyStatus;
import com.surveys.surveys.model.Survey;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
     * Busca encuestas programadas para abrir en un rango de fechas.
     *
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de encuestas programadas en el rango
     */
    List<Survey> findByScheduledOpenBetween(Instant startDate, Instant endDate);

    /**
     * Busca encuestas activas en un momento específico.
     * Una encuesta está activa si:
     * - Su estado es PUBLICADA
     * - La fecha actual está entre scheduledOpen y scheduledClose
     *
     * @param currentTime momento actual
     * @return lista de encuestas activas
     */
    @Query("{ 'status': 'PUBLICADA', 'scheduledOpen': { $lte: ?0 }, 'scheduledClose': { $gte: ?0 } }")
    List<Survey> findActiveAt(Instant currentTime);

    /**
     * Busca encuestas por estado y administrador.
     *
     * @param status estado de la encuesta
     * @param adminId ID del administrador
     * @return lista de encuestas que cumplen ambos criterios
     */
    List<Survey> findByStatusAndAdminId(SurveyStatus status, String adminId);

    /**
     * Cuenta el número de encuestas en un estado específico para un administrador.
     *
     * @param status estado de la encuesta
     * @param adminId ID del administrador
     * @return número de encuestas que cumplen los criterios
     */
    long countByStatusAndAdminId(SurveyStatus status, String adminId);

    /**
     * Busca encuestas que estén próximas a cerrar.
     * 
     * @param threshold fecha límite para considerar próximo cierre
     * @return lista de encuestas próximas a cerrar
     */
    @Query("{ 'status': 'PUBLICADA', 'scheduledClose': { $lte: ?0 } }")
    List<Survey> findSurveysNearingClose(Instant threshold);

    /**
     * Encuentra la última encuesta modificada de un administrador.
     *
     * @param adminId ID del administrador
     * @return Optional con la última encuesta modificada
     */
    Optional<Survey> findFirstByAdminIdOrderByModifiedAtDesc(String adminId);

    /**
     * Busca encuestas por nombre y estado.
     *
     * @param name fragmento del nombre a buscar
     * @param status estado de la encuesta
     * @return lista de encuestas que cumplen los criterios
     */
    List<Survey> findByNameContainingIgnoreCaseAndStatus(String name, SurveyStatus status);

    // Agregar métodos para buscar por versión anterior
    Optional<Survey> findByPreviousVersionId(String previousVersionId);

    // Agregar métodos para búsqueda de preguntas si es necesario
    List<Survey> findByQuestionsNotEmpty();
}
