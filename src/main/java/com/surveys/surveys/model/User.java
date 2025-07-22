package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Representa un usuario en el sistema.
 * Esta clase implementa UserDetails para la integración con Spring Security.
 * Los usuarios se almacenan en MongoDB en la colección "users".
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Document(collection = "users")
public class User implements UserDetails {
    
    /** Identificador único del usuario generado por MongoDB */
    @Id
    private String id;
    
    /** Nombre de usuario único en el sistema */
    @Indexed(unique = true)
    private String username;
    
    /** Contraseña encriptada del usuario */
    private String password;
    
    /** Correo electrónico único del usuario */
    @Indexed(unique = true)
    private String email;
    
    /** Roles asignados al usuario */
    private Set<String> roles = new HashSet<>();
    
    /** Estado de la cuenta del usuario */
    private boolean active = true;

    /**
     * Obtiene las autoridades (roles) del usuario.
     * Convierte los roles almacenados en objetos SimpleGrantedAuthority.
     * 
     * @return Colección de autoridades del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
} 