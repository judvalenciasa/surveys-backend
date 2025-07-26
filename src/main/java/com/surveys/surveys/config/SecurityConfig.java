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
 * <li>Filtros de seguridad</li>
 * <li>Reglas de autorización</li>
 * <li>Configuración de CORS y CSRF</li>
 * <li>Manejo de sesiones</li>
 * </ul>
 *
 * <p>
 * Esta configuración utiliza JWT para la autenticación
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
         * @param jwtAuthFilter          filtro de autenticación JWT
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
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/auth/login",
                                                                "/api/auth/register",
                                                                "/api/surveys/published",
                                                                "/api/surveys/*/view",
                                                                "/api/responses/submit")
                                                .permitAll()

                                                .requestMatchers("/api/auth/**").authenticated()

                                                .requestMatchers(
                                                                // Encuestas públicas
                                                                "/api/surveys/published",
                                                                "/api/surveys/*/view",
                                                                "/api/responses/submit")
                                                .permitAll()

                                                // Rutas de administración (Solo ADMIN)
                                                .requestMatchers(
                                                                "/api/surveys",
                                                                "/api/surveys/{id}",
                                                                "/api/surveys/search",
                                                                "/api/surveys/{id}/publish",
                                                                "/api/surveys/{id}/close",
                                                                "/api/surveys/{id}/duplicate",
                                                                "/api/surveys/{id}/branding",
                                                                "/api/surveys/{id}/version",
                                                                "/api/surveys/{id}/versions",
                                                                "/api/surveys/{surveyId}/questions/**",
                                                                "/api/responses",
                                                                "/api/responses/survey/{surveyId}")
                                                .hasRole("ADMIN")

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
