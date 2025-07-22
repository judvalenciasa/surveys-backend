package com.surveys.surveys.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuración de MongoDB para la aplicación.
 * Esta clase proporciona la configuración necesaria para conectar
 * con MongoDB y habilitar los repositorios de Spring Data MongoDB.
 * 
 * <p>La configuración incluye:
 * <ul>
 *   <li>Conexión al servidor MongoDB</li>
 *   <li>Configuración del nombre de la base de datos</li>
 *   <li>Habilitación de repositorios MongoDB</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.surveys.surveys.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    /** Nombre de la base de datos configurado en application.properties */
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    /** Host del servidor MongoDB */
    @Value("${spring.data.mongodb.host}")
    private String host;

    /** Puerto del servidor MongoDB */
    @Value("${spring.data.mongodb.port}")
    private int port;

    /**
     * {@inheritDoc}
     * @return el nombre de la base de datos a utilizar
     */
    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    /**
     * Configura y crea el cliente MongoDB.
     * 
     * @return cliente MongoDB configurado
     */
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(
            String.format("mongodb://%s:%d", host, port)
        );
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        return MongoClients.create(mongoClientSettings);
    }
}
