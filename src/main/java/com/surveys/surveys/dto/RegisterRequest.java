package com.surveys.surveys.dto;

/**
 * DTO para solicitudes de registro de usuario.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    /**
     * Constructor por defecto.
     */
    public RegisterRequest() {
    }

    /**
     * Constructor con parámetros.
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 