package com.taskflow.entity.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Password Value Object
 * 
 * En DDD, los Value Objects son objetos inmutables que representan
 * conceptos del dominio sin identidad propia.
 */
public class Password {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    
    private final String value;
    
    /**
     * Constructor privado para crear un Password
     */
    private Password(String value) {
        this.value = validate(value);
    }
    
    /**
     * Factory method para crear un Password
     */
    public static Password of(String password) {
        return new Password(password);
    }
    
    /**
     * Factory method para crear un Password con validación relajada (solo longitud mínima)
     */
    public static Password ofWeak(String password) {
        return new Password(validateWeak(password));
    }
    
    /**
     * Obtener el valor del password (solo para comparaciones internas)
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Verificar si el password coincide con otro
     */
    public boolean matches(Password other) {
        return Objects.equals(this.value, other.value);
    }
    
    /**
     * Verificar si el password es fuerte
     */
    public boolean isStrong() {
        return PASSWORD_PATTERN.matcher(value).matches();
    }
    
    /**
     * Obtener la fortaleza del password como porcentaje
     */
    public int getStrength() {
        int strength = 0;
        
        if (value.length() >= 8) strength += 20;
        if (value.length() >= 12) strength += 10;
        if (value.matches(".*[a-z].*")) strength += 20;
        if (value.matches(".*[A-Z].*")) strength += 20;
        if (value.matches(".*\\d.*")) strength += 15;
        if (value.matches(".*[@$!%*?&].*")) strength += 15;
        
        return Math.min(strength, 100);
    }
    
    /**
     * Validar password con reglas estrictas
     */
    private String validate(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                "Password must contain at least one lowercase letter, one uppercase letter, " +
                "one digit, and one special character (@$!%*?&)"
            );
        }
        
        return password;
    }
    
    /**
     * Validar password con reglas relajadas (solo longitud mínima)
     */
    private static String validateWeak(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        
        return password;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "Password{strength=" + getStrength() + "%, strong=" + isStrong() + "}";
    }
}
