package com.surveys.surveys.config;

import com.surveys.surveys.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración general de la aplicación.
 * Proporciona los beans necesarios para:
 * <ul>
 *   <li>Servicio de usuarios</li>
 *   <li>Encriptación de contraseñas</li>
 *   <li>Autenticación</li>
 * </ul>
 *
 * <p>Esta clase centraliza la configuración de componentes
 * core de la aplicación que son utilizados en múltiples lugares.
 *
 * @author TuNombre
 * @version 1.0
 * @since 2024-03-22
 */
@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Constructor que inicializa el repositorio de usuarios.
     *
     * @param userRepository repositorio para acceder a los usuarios
     */
    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Configura el servicio de detalles de usuario.
     *
     * @return servicio de detalles de usuario configurado
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    /**
     * Configura el proveedor de autenticación.
     *
     * @return proveedor de autenticación configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura el gestor de autenticación.
     *
     * @param config configuración de autenticación
     * @return gestor de autenticación configurado
     * @throws Exception si hay un error en la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura el codificador de contraseñas.
     *
     * @return codificador BCrypt configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 