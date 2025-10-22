package com.taskflow.entity.valueobject;

import java.util.Objects;

/**
 * TaskTitle Value Object
 * 
 * En DDD, los Value Objects son objetos inmutables que representan
 * conceptos del dominio sin identidad propia.
 */
public class TaskTitle {
    
    private final String value;
    
    /**
     * Constructor privado para crear un TaskTitle
     */
    private TaskTitle(String value) {
        this.value = validate(value);
    }
    
    /**
     * Factory method para crear un TaskTitle
     */
    public static TaskTitle of(String title) {
        return new TaskTitle(title);
    }
    
    /**
     * Obtener el valor del título
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Obtener la longitud del título
     */
    public int getLength() {
        return value.length();
    }
    
    /**
     * Verificar si el título está vacío
     */
    public boolean isEmpty() {
        return value.trim().isEmpty();
    }
    
    /**
     * Obtener el título truncado a una longitud específica
     */
    public String getTruncated(int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Obtener el título en mayúsculas
     */
    public String toUpperCase() {
        return value.toUpperCase();
    }
    
    /**
     * Obtener el título en minúsculas
     */
    public String toLowerCase() {
        return value.toLowerCase();
    }
    
    /**
     * Verificar si el título contiene una palabra específica
     */
    public boolean contains(String word) {
        return value.toLowerCase().contains(word.toLowerCase());
    }
    
    /**
     * Validar el título
     */
    private String validate(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        
        String trimmedTitle = title.trim();
        
        if (trimmedTitle.length() < 3) {
            throw new IllegalArgumentException("Task title must be at least 3 characters long");
        }
        
        if (trimmedTitle.length() > 200) {
            throw new IllegalArgumentException("Task title must be at most 200 characters long");
        }
        
        return trimmedTitle;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskTitle taskTitle = (TaskTitle) o;
        return Objects.equals(value, taskTitle.value);
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
