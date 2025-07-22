package com.surveys.surveys.dto;

/**
 * DTO (Data Transfer Object) que representa una solicitud de registro de usuario.
 * Contiene la información necesaria para crear una nueva cuenta de usuario.
 *
 * <p>Ejemplo de uso:
 * <pre>
 * RegisterRequest request = new RegisterRequest();
 * request.setUsername("nuevoUsuario");
 * request.setEmail("usuario@ejemplo.com");
 * request.setPassword("contraseña123");
 * </pre>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class RegisterRequest {
    /** Nombre de usuario para el nuevo registro */
    private String username;
    
    /** Dirección de correo electrónico del usuario */
    private String email;
    
    /** Contraseña para la nueva cuenta */
    private String password;

    /**
     * Constructor por defecto.
     */
    public RegisterRequest() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param username nombre de usuario
     * @param email correo electrónico
     * @param password contraseña
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
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
     * Obtiene el correo electrónico.
     * @return el correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico.
     * @param email el correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
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