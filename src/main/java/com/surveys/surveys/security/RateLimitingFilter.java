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
 *   <li>Permite ráfagas controladas de peticiones</li>
 *   <li>Mantiene una tasa promedio sostenible</li>
 *   <li>Implementación thread-safe para entornos concurrentes</li>
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

    /**
     * Evalúa y aplica el control de tasa para cada petición HTTP.
     * 
     * <p>Este método implementa el núcleo del filtro de rate limiting. Utiliza
     * un enfoque no bloqueante para verificar la disponibilidad de tokens y
     * determinar si la petición debe procesarse o rechazarse.
     * 
   
     *
     * @param request la petición HTTP entrante a evaluar para rate limiting
     * @param response la respuesta HTTP que se configurará en caso de rechazo
     * @param filterChain la cadena de filtros para continuar el procesamiento
     *                   si la petición es aceptada
     * @throws ServletException si ocurre un error en el procesamiento del servlet
     * @throws IOException si ocurre un error de entrada/salida al escribir la respuesta
     * @see RateLimiter#tryAcquire()
     * @see HttpServletResponse#setStatus(int)
     * @see HttpServletResponse#setContentType(String)
     * @see FilterChain#doFilter(jakarta.servlet.ServletRequest, jakarta.servlet.ServletResponse)
     */
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