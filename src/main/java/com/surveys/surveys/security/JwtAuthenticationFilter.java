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
 * de seguridad de Spring Security.
 * 
 * <p>
 * Este filtro:
 * <ul>
 *   <li>Verificación de rutas excluidas del filtrado</li>
 *   <li>Extracción del token JWT del header {@code Authorization: Bearer <token>}</li>
 *   <li>Validación del token usando {@link JwtService#isTokenValid(String, UserDetails)}</li>
 *   <li>Carga de los detalles del usuario desde {@link UserDetailsService}</li>
 *   <li>Establecimiento de la autenticación en {@link SecurityContextHolder}</li>
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

    /**
     * Servicio para operaciones con tokens JWT.
     * Utilizado para extraer información y validar tokens.
     */
    private final JwtService jwtService;
    
    /**
     * Servicio para cargar detalles de usuarios.
     * Utilizado para obtener información completa del usuario desde el username.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor que inicializa el filtro con sus dependencias requeridas.
     * 
     * <p>Las dependencias son inyectadas automáticamente por Spring Boot
     * mediante inyección de dependencias por constructor.
     *
     * @param jwtService servicio para manejo de tokens JWT, no debe ser {@code null}
     * @param userDetailsService servicio para cargar detalles de usuarios, no debe ser {@code null}
     * @throws IllegalArgumentException si alguna dependencia es {@code null}
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Determina si una petición HTTP debe ser excluida del filtrado de autenticación.
     * 
     * <p>Este método verifica si la ruta de la petición corresponde a endpoints
     * públicos que no requieren autenticación. Las rutas excluidas incluyen
     * todos los endpoints de autenticación y registros, así como endpoints
     * específicamente marcados como públicos.
     * 
     * @param request la petición HTTP entrante
     * @return {@code true} si la petición debe ser excluida del filtrado;
     *         {@code false} si debe procesarse normalmente
     * @throws IllegalArgumentException si request es {@code null}
     * @see HttpServletRequest#getServletPath()
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || path.startsWith("/api/public/");
    }

    /**
     * Procesa la autenticación JWT para cada petición HTTP válida.
     * 
     * <p>Este método implementa el núcleo del filtro de autenticación JWT.
     * Ejecuta el siguiente flujo de procesamiento:
     * 
     * 
     * <p><strong>Thread Safety:</strong> Este método es thread-safe y puede manejar
     * múltiples peticiones concurrentes de forma segura.
     *
     * @param request la petición HTTP entrante que contiene el token JWT
     * @param response la respuesta HTTP que se enviará al cliente
     * @param filterChain la cadena de filtros para continuar el procesamiento
     * @throws ServletException si ocurre un error en el procesamiento del servlet
     * @throws IOException si ocurre un error de entrada/salida durante el procesamiento
     * @see JwtService#extractUsername(String)
     * @see JwtService#isTokenValid(String, UserDetails)
     * @see UserDetailsService#loadUserByUsername(String)
     * @see SecurityContextHolder#getContext()
     * @see UsernamePasswordAuthenticationToken
     * @see WebAuthenticationDetailsSource
     */
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