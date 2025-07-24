package com.surveys.surveys.repository;

import com.surveys.surveys.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

} 