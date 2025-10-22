package com.taskflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User Aggregate Root
 * 
 * En DDD, esta es la raíz del agregado User.
 * Contiene la lógica de negocio y coordina las operaciones del dominio.
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones - Solo referencias, no colecciones completas
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();
    
    // Constructores
    protected User() {
        // Constructor protegido para JPA
    }
    
    /**
     * Constructor de dominio para crear un nuevo usuario
     * Este es el constructor principal que debe usarse en el dominio
     */
    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = validateUsername(username);
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = UserStatus.ACTIVE;
    }
    
    // Métodos de negocio (Domain Methods)
    
    /**
     * Cambiar el estado del usuario a inactivo
     * Regla de negocio: Un usuario inactivo no puede tener proyectos activos
     */
    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive");
        }
        
        // Verificar que no tenga proyectos activos
        boolean hasActiveProjects = projects.stream()
            .anyMatch(project -> project.getStatus() == Project.ProjectStatus.ACTIVE);
            
        if (hasActiveProjects) {
            throw new IllegalStateException("Cannot deactivate user with active projects");
        }
        
        this.status = UserStatus.INACTIVE;
    }
    
    /**
     * Reactivar un usuario
     */
    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        
        this.status = UserStatus.ACTIVE;
    }
    
    /**
     * Actualizar información del perfil
     * Regla de negocio: Solo se puede actualizar si el usuario está activo
     */
    public void updateProfile(String firstName, String lastName) {
        if (this.status != UserStatus.ACTIVE) {
            throw new IllegalStateException("Cannot update profile of inactive user");
        }
        
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     * Cambiar contraseña
     * Regla de negocio: La nueva contraseña debe ser diferente a la actual
     */
    public void changePassword(String currentPassword, String newPassword) {
        if (this.status != UserStatus.ACTIVE) {
            throw new IllegalStateException("Cannot change password of inactive user");
        }
        
        if (!Objects.equals(this.password, currentPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        if (Objects.equals(this.password, newPassword)) {
            throw new IllegalArgumentException("New password must be different from current password");
        }
        
        this.password = validatePassword(newPassword);
    }
    
    /**
     * Obtener el nombre completo del usuario
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return username;
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
    
    /**
     * Verificar si el usuario puede crear proyectos
     * Regla de negocio: Solo usuarios activos pueden crear proyectos
     */
    public boolean canCreateProjects() {
        return this.status == UserStatus.ACTIVE;
    }
    
    // Métodos de validación privados
    
    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        return username.trim();
    }
    
    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must be valid");
        }
        return email.trim().toLowerCase();
    }
    
    private String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        return password;
    }
    
    // Getters (solo los necesarios para el dominio)
    
    public Long getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Setters solo para JPA (protegidos)
    
    protected void setId(Long id) {
        this.id = id;
    }
    
    protected void setUsername(String username) {
        this.username = username;
    }
    
    protected void setEmail(String email) {
        this.email = email;
    }
    
    protected void setPassword(String password) {
        this.password = password;
    }
    
    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    protected void setStatus(UserStatus status) {
        this.status = status;
    }
    
    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Equals y HashCode basados en identidad de negocio
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", status=" + status +
                '}';
    }
    
    // Enums del dominio
    
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}