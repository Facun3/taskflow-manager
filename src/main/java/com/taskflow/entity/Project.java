package com.taskflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Project Aggregate Root
 * 
 * En DDD, esta es la raíz del agregado Project.
 * Coordina las operaciones relacionadas con proyectos y sus tareas.
 */
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Project name is required")
    @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();
    
    // Constructores
    protected Project() {
        // Constructor protegido para JPA
    }
    
    /**
     * Constructor de dominio para crear un nuevo proyecto
     */
    public Project(String name, String description, User owner) {
        this.name = validateName(name);
        this.description = description;
        this.owner = validateOwner(owner);
        this.status = ProjectStatus.ACTIVE;
    }
    
    // Métodos de negocio (Domain Methods)
    
    /**
     * Actualizar información del proyecto
     * Regla de negocio: Solo se puede actualizar si el proyecto está activo
     */
    public void updateProject(String name, String description) {
        if (this.status != ProjectStatus.ACTIVE) {
            throw new IllegalStateException("Cannot update inactive or completed project");
        }
        
        this.name = validateName(name);
        this.description = description;
    }
    
    /**
     * Completar el proyecto
     * Regla de negocio: Solo se puede completar si todas las tareas están completadas o canceladas
     */
    public void complete() {
        if (this.status == ProjectStatus.COMPLETED) {
            throw new IllegalStateException("Project is already completed");
        }
        
        if (this.status == ProjectStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot complete an archived project");
        }
        
        // Verificar que todas las tareas estén completadas o canceladas
        boolean hasIncompleteTasks = tasks.stream()
            .anyMatch(task -> task.getStatus() == Task.TaskStatus.TODO || 
                            task.getStatus() == Task.TaskStatus.IN_PROGRESS);
            
        if (hasIncompleteTasks) {
            throw new IllegalStateException("Cannot complete project with incomplete tasks");
        }
        
        this.status = ProjectStatus.COMPLETED;
    }
    
    /**
     * Archivar el proyecto
     * Regla de negocio: Solo se puede archivar si el proyecto está completado
     */
    public void archive() {
        if (this.status == ProjectStatus.ARCHIVED) {
            throw new IllegalStateException("Project is already archived");
        }
        
        if (this.status != ProjectStatus.COMPLETED) {
            throw new IllegalStateException("Can only archive completed projects");
        }
        
        this.status = ProjectStatus.ARCHIVED;
    }
    
    /**
     * Reactivar un proyecto archivado
     */
    public void reactivate() {
        if (this.status != ProjectStatus.ARCHIVED) {
            throw new IllegalStateException("Can only reactivate archived projects");
        }
        
        this.status = ProjectStatus.ACTIVE;
    }
    
    /**
     * Agregar una tarea al proyecto
     * Regla de negocio: Solo se pueden agregar tareas a proyectos activos
     */
    public void addTask(Task task) {
        if (this.status != ProjectStatus.ACTIVE) {
            throw new IllegalStateException("Cannot add tasks to inactive or completed projects");
        }
        
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        
        if (!tasks.contains(task)) {
            tasks.add(task);
            task.setProject(this);
        }
    }
    
    /**
     * Remover una tarea del proyecto
     */
    public void removeTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        
        if (tasks.remove(task)) {
            task.setProject(null);
        }
    }
    
    /**
     * Obtener el progreso del proyecto basado en las tareas completadas
     */
    public double getProgress() {
        if (tasks.isEmpty()) {
            return 0.0;
        }
        
        long completedTasks = tasks.stream()
            .filter(task -> task.getStatus() == Task.TaskStatus.COMPLETED)
            .count();
            
        return (double) completedTasks / tasks.size() * 100;
    }
    
    /**
     * Obtener el número total de tareas
     */
    public int getTotalTasks() {
        return tasks.size();
    }
    
    /**
     * Obtener el número de tareas completadas
     */
    public int getCompletedTasks() {
        return (int) tasks.stream()
            .filter(task -> task.getStatus() == Task.TaskStatus.COMPLETED)
            .count();
    }
    
    /**
     * Obtener el número de tareas pendientes
     */
    public int getPendingTasks() {
        return (int) tasks.stream()
            .filter(task -> task.getStatus() == Task.TaskStatus.TODO || 
                           task.getStatus() == Task.TaskStatus.IN_PROGRESS)
            .count();
    }
    
    /**
     * Verificar si el proyecto puede ser editado
     */
    public boolean canBeEdited() {
        return this.status == ProjectStatus.ACTIVE;
    }
    
    /**
     * Verificar si el proyecto puede tener tareas agregadas
     */
    public boolean canAcceptTasks() {
        return this.status == ProjectStatus.ACTIVE;
    }
    
    // Métodos de validación privados
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new IllegalArgumentException("Project name must be between 3 and 100 characters");
        }
        return name.trim();
    }
    
    private User validateOwner(User owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Project owner cannot be null");
        }
        if (!owner.canCreateProjects()) {
            throw new IllegalArgumentException("User cannot create projects");
        }
        return owner;
    }
    
    // Getters (solo los necesarios para el dominio)
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ProjectStatus getStatus() {
        return status;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public List<Task> getTasks() {
        return new ArrayList<>(tasks); // Retorna una copia para evitar modificaciones externas
    }
    
    // Setters solo para JPA (protegidos)
    
    protected void setId(Long id) {
        this.id = id;
    }
    
    protected void setName(String name) {
        this.name = name;
    }
    
    protected void setDescription(String description) {
        this.description = description;
    }
    
    protected void setStatus(ProjectStatus status) {
        this.status = status;
    }
    
    protected void setOwner(User owner) {
        this.owner = owner;
    }
    
    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    protected void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? new ArrayList<>(tasks) : new ArrayList<>();
    }
    
    // Equals y HashCode basados en identidad de negocio
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", owner=" + (owner != null ? owner.getUsername() : "null") +
                ", progress=" + String.format("%.1f%%", getProgress()) +
                '}';
    }
    
    // Enums del dominio
    
    public enum ProjectStatus {
        ACTIVE, COMPLETED, ARCHIVED
    }
}
