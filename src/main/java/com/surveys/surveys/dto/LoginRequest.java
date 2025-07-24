package com.surveys.surveys.dto;

/**
 * DTO para solicitudes de inicio de sesión.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Constructor por defecto.
     */
    public LoginRequest() {
    }

    /**
     * Constructor con parámetros.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 