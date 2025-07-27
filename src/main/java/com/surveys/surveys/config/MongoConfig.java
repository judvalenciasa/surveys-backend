package com.surveys.surveys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuración de MongoDB para la aplicación.
 * Esta clase habilita los repositorios de Spring Data MongoDB
 * y permite que Spring Boot use la URI de MongoDB automáticamente.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.surveys.surveys.repository")
public class MongoConfig {
    // Spring Boot configurará automáticamente MongoDB usando spring.data.mongodb.uri
}
