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
 * 
 * <p>Esta clase encapsula toda la lógica relacionada con la creación, validación
 * y extracción de información de tokens JWT utilizados para la autenticación
 * en el sistema de encuestas. Proporciona una interfaz completa para el manejo
 * seguro de tokens con soporte para invalidación mediante logout.
 * 
 * <p>Las principales funcionalidades incluyen:
 * <ul>
 *   <li>Generación de tokens JWT firmados con algoritmo HS512</li>
 *   <li>Validación de tokens JWT considerando expiración y logout</li>
 *   <li>Extracción segura de claims y información del usuario</li>
 *   <li>Manejo robusto de fechas de expiración e invalidación</li>
 * </ul>
 * 

 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2024-03-22
 * @see UserDetails
 * @see User
 * @see Claims
 */
@Service
public class JwtService {
    
    /**
     * Clave secreta para firmar y validar tokens JWT.
     * Debe ser una cadena de al menos 256 bits para HS512.
     */
    @Value("${jwt.secret}")
    private String secretKey;
    
    /**
     * Tiempo de expiración de los tokens en milisegundos.
     * Valor por defecto recomendado: 86400000 (24 horas).
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     * 
     * <p>Este método utiliza el claim "sub" (subject) del token para
     * obtener el identificador único del usuario autenticado.
     *
     * @param token el token JWT del cual extraer el username
     * @return el nombre de usuario contenido en el token
     * @throws JwtException si el token es inválido o está malformado
     * @throws IllegalArgumentException si el token es null o vacío
     * @see Claims#getSubject()
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Genera un token JWT básico para un usuario autenticado.
     * 
     * <p>Crea un token estándar con claims mínimos: subject, issued at
     * y expiration. No incluye claims adicionales personalizados.
     *
     * @param userDetails los detalles del usuario para incluir en el token
     * @return el token JWT generado como una cadena codificada
     * @throws IllegalArgumentException si userDetails es null
     * @see #generateToken(Map, UserDetails)
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims adicionales personalizados.
     * 
     * <p>Crea un token JWT firmado digitalmente que incluye:
     * <ul>
     *   <li>Claims personalizados proporcionados</li>
     *   <li>Subject (username del usuario)</li>
     *   <li>Fecha de emisión (momento actual)</li>
     *   <li>Fecha de expiración (calculada según configuración)</li>
     * </ul>
     * 
     * <p>El token se firma utilizando el algoritmo HS512 con la clave
     * secreta configurada, garantizando su integridad y autenticidad.
     *
     * @param extraClaims mapa de claims adicionales a incluir en el token.
     *                   Puede estar vacío pero no debe ser null
     * @param userDetails los detalles del usuario que serán el subject del token
     * @return el token JWT generado como una cadena Base64 URL codificada
     * @throws IllegalArgumentException si algún parámetro es null
     * @throws JwtException si ocurre un error durante la generación del token
     * @see SignatureAlgorithm#HS512
     */
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
     * <p>Realiza una validación completa que incluye:
     * <ul>
     *   <li>Verificación de la firma del token</li>
     *   <li>Comprobación de que el username coincide</li>
     *   <li>Validación de que el token no ha expirado</li>
     *   <li>Verificación contra el último logout del usuario</li>
     * </ul>
     * 
     * <p><strong>Invalidación por logout:</strong> Si el usuario es una
     * instancia de {@link User} y tiene una fecha de último logout posterior
     * a la emisión del token, el token se considera inválido. Esto previene
     * el uso de tokens después de cerrar sesión explícitamente.
     *
     * @param token el token JWT a validar
     * @param userDetails los detalles del usuario contra el cual validar
     * @return {@code true} si el token es válido y puede ser usado para
     *         autenticación; {@code false} en caso contrario
     * @throws IllegalArgumentException si algún parámetro es null
     * @see User#getLastLogout()
     * @see Claims#getIssuedAt()
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
     * Extrae un claim específico del token JWT utilizando un resolver personalizado.
     * 
     * <p>Este es un método genérico que permite extraer cualquier claim
     * del token aplicando una función resolver. Es la base para métodos
     * más específicos como {@link #extractUsername(String)}.
     * 
     * <p>Ejemplos de uso:
     * <pre>{@code
     * // Extraer el subject
     * String username = extractClaim(token, Claims::getSubject);
     * 
     * // Extraer fecha de expiración
     * Date expiration = extractClaim(token, Claims::getExpiration);
     * 
     * // Extraer claim personalizado
     * String customClaim = extractClaim(token, claims -> claims.get("customKey", String.class));
     * }</pre>
     *
     * @param <T> el tipo de dato del claim a extraer
     * @param token el token JWT del cual extraer el claim
     * @param claimsResolver función que define cómo extraer el claim deseado
     * @return el valor del claim extraído del tipo especificado
     * @throws JwtException si el token es inválido, malformado o la firma no coincide
     * @throws IllegalArgumentException si algún parámetro es null
     * @see Claims
     * @see Function
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Verifica si un token JWT ha expirado.
     * 
     * <p>Compara la fecha de expiración del token con la fecha actual
     * del sistema para determinar si el token sigue siendo válido
     * temporalmente.
     *
     * @param token el token JWT a verificar
     * @return {@code true} si el token ha expirado; {@code false} si sigue válido
     * @throws JwtException si el token es inválido o malformado
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración de un token JWT.
     * 
     * @param token el token JWT del cual extraer la fecha de expiración
     * @return la fecha de expiración del token
     * @throws JwtException si el token es inválido o malformado
     * @see Claims#getExpiration()
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todos los claims de un token JWT.
     * 
     * <p>Parsea completamente el token JWT y valida su firma antes de
     * retornar el cuerpo con todos los claims. Este método es la base
     * para todas las operaciones de extracción de claims.
     * 
     * <p><strong>Seguridad:</strong> La validación de la firma garantiza
     * que el token no ha sido modificado desde su creación.
     *
     * @param token el token JWT a parsear
     * @return objeto Claims conteniendo todos los claims del token
     * @throws JwtException si el token es inválido, malformado o la firma no coincide
     * @throws SecurityException si la clave de firma no es válida
     * @see Jwts#parserBuilder()
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma para tokens JWT.
     * 
     * <p>Convierte la clave secreta configurada en un objeto Key apropiado
     * para el algoritmo HS512. La clave debe tener al menos 256 bits para
     * garantizar la seguridad adecuada.
     * 
     * <p><strong>Importante:</strong> La clave secreta debe mantenerse segura
     * y no debe exponerse en logs o respuestas de error.
     *
     * @return objeto Key derivado de la clave secreta configurada
     * @throws IllegalArgumentException si la clave secreta es demasiado corta
     * @see Keys#hmacShaKeyFor(byte[])
     * @see SignatureAlgorithm#HS512
     */
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 