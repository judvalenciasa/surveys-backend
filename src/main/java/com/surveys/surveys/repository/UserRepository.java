package com.surveys.surveys.repository;

import com.surveys.surveys.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByActive(boolean active);
    
    List<User> findByRolesContaining(String role);
    
    Optional<User> findByEmailIgnoreCase(String email);
    
    long countByRolesContaining(String role);

    List<User> findByEmployeeId(String employeeId);
    
    List<User> findByDepartmentId(String departmentId);
    
    @Query("{'metadata.lastLogin': {$lt: ?0}}")
    List<User> findInactiveUsers(Instant date);
    
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    List<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

    @Query(value = "{}", sort = "{'metadata.lastLogin': -1}")
    List<User> findFirst10ByOrderByLastLoginDesc();
} 