package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Date;

/**
 * Representa un usuario en el sistema.
 * Esta clase implementa UserDetails para la integración con Spring Security.
 * Los usuarios se almacenan en MongoDB en la colección "users".
 */
@Document(collection = "users")
public class User implements UserDetails {
    
    @Id
    private String id;
    

    @Indexed(unique = true)
    private String username;
    
    private String password;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Indexed(unique = true)
    private String email;
    
    private Set<String> roles = new HashSet<>();
    
    private String firstName;
    
    private String lastName;
    
    @Indexed
    private String employeeId;
    
    private String departmentId;
    
    private String position;
    
    private UserMetadata metadata;
    
    private boolean active = true;

    private Date lastLogout;

    // Clase interna para manejar los metadatos
    public static class UserMetadata {
        private Instant lastLogin;
        private Instant lastSurveyCompleted;
        private int surveysCompleted;

        // Getters y Setters
        public Instant getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(Instant lastLogin) {
            this.lastLogin = lastLogin;
        }

        public Instant getLastSurveyCompleted() {
            return lastSurveyCompleted;
        }

        public void setLastSurveyCompleted(Instant lastSurveyCompleted) {
            this.lastSurveyCompleted = lastSurveyCompleted;
        }

        public int getSurveysCompleted() {
            return surveysCompleted;
        }

        public void setSurveysCompleted(int surveysCompleted) {
            this.surveysCompleted = surveysCompleted;
        }
    }

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
    public String getId() { 
        return id; 
    }
    
    public void setId(String id) { 
        this.id = id; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public Set<String> getRoles() { 
        return roles; 
    }
    
    public void setRoles(Set<String> roles) { 
        this.roles = roles; 
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public UserMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(UserMetadata metadata) {
        this.metadata = metadata;
    }

    public boolean isActive() { 
        return active; 
    }
    
    public void setActive(boolean active) { 
        this.active = active; 
    }

    public Date getLastLogout() {
        return lastLogout;
    }
    
    public void setLastLogout(Date lastLogout) {
        this.lastLogout = lastLogout;
    }
} 