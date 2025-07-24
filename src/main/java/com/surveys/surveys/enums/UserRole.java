package com.surveys.surveys.enums;

/**
 * Roles de usuario en el sistema.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public enum UserRole {
    /** Administrador - acceso completo */
    ADMIN("ADMIN"),
    
    /** Empleado - acceso limitado */
    EMPLOYEE("EMPLOYEE"),
    
    /** Usuario básico - solo responder encuestas */
    USER("USER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    /**
     * Obtiene el valor del rol como String.
     */
    public String getRole() {
        return role;
    }
}
