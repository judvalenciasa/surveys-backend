package com.surveys.surveys.repository;

import com.surveys.surveys.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de usuarios del sistema.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Busca usuario por nombre de usuario.
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Verifica si existe un nombre de usuario.
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un email.
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuarios por email o username (case insensitive).
     */
    List<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);
} 