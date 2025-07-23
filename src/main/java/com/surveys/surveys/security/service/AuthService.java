package com.surveys.surveys.security.service;

import com.surveys.surveys.dto.LoginRequest;
import com.surveys.surveys.dto.RegisterRequest;
import com.surveys.surveys.model.User;
import com.surveys.surveys.repository.UserRepository;
import com.surveys.surveys.security.dto.AuthResponse;
import com.surveys.surveys.security.JwtService;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que maneja la autenticación y registro de usuarios.
 * Proporciona funcionalidades para registro de nuevos usuarios y login.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor que inicializa el servicio con sus dependencias.
     * 
     * @param userRepository Repositorio para operaciones con usuarios
     * @param passwordEncoder Codificador para encriptar contraseñas
     * @param jwtService Servicio para manejo de tokens JWT
     * @param authenticationManager Gestor de autenticación de Spring
     */
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param request DTO con la información del nuevo usuario
     * @return AuthResponse con el token JWT y datos del usuario
     * @throws RuntimeException si el username o email ya existen
     */
    public AuthResponse register(RegisterRequest request) {
        // Validaciones
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
         
        // Asignar roles
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        // Guardar usuario
        try {
            User savedUser = userRepository.save(user);
            String token = jwtService.generateToken(savedUser);
            return new AuthResponse(token, savedUser.getUsername(), savedUser.getRoles());
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage());
        }
    }
    
    /**
     * Autentica un usuario existente.
     * 
     * @param request DTO con las credenciales del usuario
     * @return AuthResponse con el token JWT y datos del usuario
     * @throws RuntimeException si las credenciales son inválidas
     */
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        
        return new AuthResponse(token, user.getUsername(), user.getRoles());
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        if (jwtService.isTokenValid(refreshToken, user)) {
            String newToken = jwtService.generateToken(user);
            return new AuthResponse(newToken, user.getUsername(), user.getRoles());
        }
        throw new RuntimeException("Refresh token inválido");
    }

    @Transactional
    public void logout(String token) {
        try {
            // 1. Validar que el token sea válido
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!jwtService.isTokenValid(token, user)) {
                throw new RuntimeException("Token inválido");
            }

            // 2. Actualizar la versión del token del usuario
            // Esto invalidará todos los tokens existentes
            user.setLastLogout(new Date());
            userRepository.save(user);

            // 3. Limpiar el contexto de seguridad
            SecurityContextHolder.clearContext();

        } catch (Exception e) {
            throw new RuntimeException("Error en el proceso de logout: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            return jwtService.isTokenValid(token, user);
        } catch (Exception e) {
            return false;
        }
    }

    public User getCurrentUser(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
} 