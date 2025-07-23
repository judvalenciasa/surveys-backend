package com.surveys.surveys.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro de autenticación JWT que intercepta todas las peticiones HTTP
 * para validar tokens JWT y establecer la autenticación en el contexto
 * de seguridad de Spring.
 * 
 * <p>
 * Este filtro:
 * <ul>
 *   <li>Extrae el token JWT del header Authorization</li>
 *   <li>Valida el token usando {@link JwtService}</li>
 *   <li>Carga los detalles del usuario</li>
 *   <li>Establece la autenticación en el SecurityContextHolder</li>
 * </ul>
 * 
 * <p>
 * El filtro ignora ciertas rutas públicas como:
 * <ul>
 *   <li>/api/auth/** - Endpoints de autenticación</li>
 *   <li>/api/public/** - Endpoints públicos</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.1
 * @since 2024-03-22
 * @see JwtService
 * @see SecurityContextHolder
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || path.startsWith("/api/public/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
            
        // Si la ruta debe ser ignorada, continuar con la cadena de filtros
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        
        // Si no hay header de autorización o no es del tipo Bearer, continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extraer y validar el token
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);

            // Si hay un username y no hay autenticación previa
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Validar el token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Crear el token de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                    // Establecer los detalles de la autenticación
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            
            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            // En caso de error, enviar respuesta de no autorizado
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
        }
    }
} 