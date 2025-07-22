package com.surveys.surveys.repository;

import com.surveys.surveys.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

/**
 * Repositorio para la gestión de usuarios en MongoDB.
 * Proporciona operaciones CRUD y consultas personalizadas para el manejo de usuarios.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Autenticación de usuarios</li>
 *   <li>Validación de credenciales únicas</li>
 *   <li>Gestión de roles</li>
 *   <li>Control de estado de usuarios</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca un usuario por su nombre de usuario.
     * Método principal usado en la autenticación.
     *
     * @param username nombre de usuario a buscar
     * @return Optional con el usuario si existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     * Usado en validaciones durante el registro.
     *
     * @param username nombre de usuario a verificar
     * @return true si el username ya existe
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email especificado.
     * Usado en validaciones durante el registro.
     *
     * @param email email a verificar
     * @return true si el email ya existe
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por su estado de activación.
     *
     * @param active estado de activación
     * @return lista de usuarios según su estado
     */
    List<User> findByActive(boolean active);

    /**
     * Busca usuarios que tengan un rol específico.
     *
     * @param role rol a buscar
     * @return lista de usuarios con el rol especificado
     */
    List<User> findByRolesContaining(String role);

    /**
     * Busca usuarios por email, ignorando mayúsculas y minúsculas.
     *
     * @param email email a buscar
     * @return Optional con el usuario si existe
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Cuenta el número de usuarios con un rol específico.
     *
     * @param role rol a contar
     * @return número de usuarios con el rol
     */
    long countByRolesContaining(String role);

    /**
     * Busca usuarios creados en un rango de fechas.
     *
     * @param start fecha inicial
     * @param end fecha final
     * @return lista de usuarios creados en el rango
     */
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Busca usuarios por nombre de usuario parcial.
     *
     * @param username fragmento del nombre de usuario
     * @return lista de usuarios que coinciden
     */
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * Encuentra usuarios inactivos que no se han conectado desde una fecha.
     *
     * @param date fecha límite de última conexión
     * @return lista de usuarios inactivos
     */
    @Query("{'active': true, 'lastLoginDate': {$lt: ?0}}")
    List<User> findInactiveUsers(LocalDateTime date);

    /**
     * Busca usuarios que necesitan verificar su email.
     *
     * @return lista de usuarios pendientes de verificación
     */
    @Query("{'emailVerified': false}")
    List<User> findUnverifiedUsers();

    /**
     * Actualiza el estado de activación de un usuario.
     *
     * @param userId ID del usuario
     * @param active nuevo estado de activación
     * @return true si se actualizó correctamente
     */
    @Query("{'_id': ?0}")
    boolean updateUserActiveStatus(String userId, boolean active);

    /**
     * Busca usuarios por email o nombre de usuario.
     *
     * @param email email a buscar
     * @param username nombre de usuario a buscar
     * @return lista de usuarios que coinciden con alguno de los criterios
     */
    List<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

    /**
     * Encuentra los últimos usuarios registrados.
     *
     * @param limit número máximo de usuarios a retornar
     * @return lista de usuarios ordenados por fecha de creación
     */
    List<User> findTop10ByOrderByCreatedAtDesc();
} 