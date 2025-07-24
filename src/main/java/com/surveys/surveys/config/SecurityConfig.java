package com.surveys.surveys.config;

import com.surveys.surveys.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Configuración de seguridad de la aplicación.
 * Define la configuración de Spring Security, incluyendo:
 * <ul>
 *   <li>Filtros de seguridad</li>
 *   <li>Reglas de autorización</li>
 *   <li>Configuración de CORS y CSRF</li>
 *   <li>Manejo de sesiones</li>
 * </ul>
 *
 * <p>Esta configuración utiliza JWT para la autenticación
 * y establece una política stateless para las sesiones.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Constructor que inicializa los componentes de seguridad.
     *
     * @param jwtAuthFilter filtro de autenticación JWT
     * @param authenticationProvider proveedor de autenticación
     */
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthFilter,
            AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * 
     * @param http configuración de seguridad HTTP
     * @return cadena de filtros configurada
     * @throws Exception si hay un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ========================================
                // RUTAS PÚBLICAS (Sin autenticación)
                // ========================================
                
                // Autenticación
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                // 🌍 ENCUESTAS PÚBLICAS - Acceso sin autenticación
                .requestMatchers(
                    "/api/surveys/published",           
                    "/api/surveys/*/view",              
                    "/api/surveys/*/public"           
                ).permitAll()
                
                .requestMatchers(
                    "/api/surveys/*/respond",           // Responder encuesta específica
                    "/api/responses/submit"            // Enviar respuesta
                ).permitAll()
                
                // ========================================
                // RUTAS DE ADMINISTRACIÓN (Solo ADMIN)
                // ========================================
                .requestMatchers(
                    // Gestión completa de encuestas
                    "/api/surveys",                                    // POST - Crear
                    "/api/surveys/{id}",                              // GET, PUT, DELETE
                    "/api/surveys/{surveyId}/questions",              // POST, GET - Preguntas
                    "/api/surveys/{surveyId}/questions/{questionId}", // GET, PUT, DELETE
                    "/api/surveys/{id}/publish",                      // POST - Publicar
                    "/api/surveys/{id}/close",                        // POST - Cerrar
                    "/api/surveys/templates/**",                      // Plantillas
                    "/api/surveys/{id}/branding",                     // PATCH - Branding
                    "/api/surveys/{id}/schedule",                     // POST - Programar
                    "/api/surveys/{id}/version",                      // POST - Nueva versión
                    "/api/surveys/{id}/versions",                     // GET - Historial
                    "/api/surveys/search",                            // GET - Búsqueda admin
                    
                    // Gestión completa de respuestas (solo lectura/administración)
                    "/api/responses",                                 // GET, POST - Admin
                    "/api/responses/{id}",                           // GET, PUT, DELETE
                    "/api/responses/survey/{surveyId}",              // GET - Por encuesta
                    "/api/responses/date-range",                     // GET - Por fecha
                    "/api/responses/survey/{surveyId}/date-range",   // GET - Combinado
                    "/api/responses/survey/{surveyId}/count",        // GET - Contador
                    "/api/responses/survey/{surveyId}/latest"        // GET - Últimas
                ).hasRole("ADMIN")
                .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura las políticas CORS.
     * 
     * @return origen de configuración CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
