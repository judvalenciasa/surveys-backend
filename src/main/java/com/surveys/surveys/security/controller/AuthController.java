package com.surveys.surveys.security.controller;

import com.surveys.surveys.dto.LoginRequest;
import com.surveys.surveys.dto.RegisterRequest;
import com.surveys.surveys.security.dto.AuthResponse;
import com.surveys.surveys.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de autenticación.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
    origins = "*",
    methods = {
        RequestMethod.POST,
        RequestMethod.GET,
        RequestMethod.OPTIONS
    },
    allowedHeaders = {
        "Authorization",
        "Content-Type"
    },
    exposedHeaders = "Authorization"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            authService.logout(token.substring(7));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
} 