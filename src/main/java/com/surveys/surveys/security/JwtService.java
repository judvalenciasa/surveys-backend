package com.surveys.surveys.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import com.surveys.surveys.model.User;

/**
 * Servicio que maneja la generación y validación de tokens JWT (JSON Web Token).
 * Esta clase encapsula toda la lógica relacionada con la creación, validación
 * y extracción de información de tokens JWT utilizados para la autenticación.
 * 
 * <p>
 * Las principales funcionalidades incluyen:
 * <ul>
 *   <li>Generación de tokens JWT para usuarios autenticados</li>
 *   <li>Validación de tokens JWT recibidos</li>
 *   <li>Extracción de claims y username del token</li>
 *   <li>Manejo de fechas de expiración</li>
 * </ul>
 * 

 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2024-03-22
 */
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Valida si un token JWT es válido para un usuario específico.
     *
     * @param token token JWT a validar
     * @param userDetails detalles del usuario
     * @return true si el token es válido
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            
            // Verificar si el token fue emitido antes del último logout
            if (userDetails instanceof User) {
                User user = (User) userDetails;
                Date lastLogout = user.getLastLogout();
                Date tokenIssuedAt = extractClaim(token, Claims::getIssuedAt);
                
                if (lastLogout != null && tokenIssuedAt != null && 
                    tokenIssuedAt.before(lastLogout)) {
                    return false;
                }
            }
            
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae un claim específico del token.
     *
     * @param token token JWT
     * @param claimsResolver función para extraer el claim
     * @return valor del claim extraído
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 