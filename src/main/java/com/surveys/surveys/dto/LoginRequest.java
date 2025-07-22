package com.surveys.surveys.dto;

/**
 * DTO (Data Transfer Object) que representa una solicitud de inicio de sesión.
 * Contiene las credenciales necesarias para autenticar a un usuario.
 *
 * <p>Ejemplo de uso:
 * <pre>
 * LoginRequest request = new LoginRequest();
 * request.setUsername("usuario");
 * request.setPassword("contraseña");
 * </pre>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class LoginRequest {
    /** Nombre de usuario para la autenticación */
    private String username;
    
    /** Contraseña del usuario */
    private String password;

    /**
     * Constructor por defecto.
     */
    public LoginRequest() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * @param username el nombre de usuario a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña.
     * @return la contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     * @param password la contraseña a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }
} 