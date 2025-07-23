package com.surveys.surveys.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    EMPLOYEE("EMPLOYEE"),
    USER("USER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
