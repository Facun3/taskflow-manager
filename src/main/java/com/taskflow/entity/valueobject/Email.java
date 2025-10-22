package com.taskflow.entity.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email Value Object
 * 
 * En DDD, los Value Objects son objetos inmutables que representan
 * conceptos del dominio sin identidad propia.
 */
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private final String value;
    
    /**
     * Constructor privado para crear un Email
     */
    private Email(String value) {
        this.value = validateAndNormalize(value);
    }
    
    /**
     * Factory method para crear un Email
     */
    public static Email of(String email) {
        return new Email(email);
    }
    
    /**
     * Obtener el valor del email
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Obtener el dominio del email
     */
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
    
    /**
     * Obtener la parte local del email (antes del @)
     */
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }
    
    /**
     * Verificar si el email es de un dominio específico
     */
    public boolean isFromDomain(String domain) {
        return getDomain().equalsIgnoreCase(domain);
    }
    
    /**
     * Validar y normalizar el email
     */
    private String validateAndNormalize(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        String normalizedEmail = email.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        
        return normalizedEmail;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
