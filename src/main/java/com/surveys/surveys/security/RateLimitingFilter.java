package com.surveys.surveys.security;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtro para limitar la tasa de peticiones por usuario/IP.
 * Implementa control de tasa para prevenir ataques de fuerza bruta y DoS.
 *
 * <p>Características:
 * <ul>
 *   <li>Límite de peticiones por segundo</li>
 *   <li>Control por IP</li>
 *   <li>Respuestas de error personalizadas</li>
 *   <li>Configuración flexible de límites</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private final RateLimiter rateLimiter;
    
    public RateLimitingFilter() {
        // Configura 10 peticiones por segundo
        this.rateLimiter = RateLimiter.create(10.0);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Demasiadas peticiones. Por favor, intente más tarde.\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
} 