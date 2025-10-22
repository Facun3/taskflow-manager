package com.taskflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Task Entity
 * 
 * En DDD, esta es una entidad dentro del agregado Project.
 * Las tareas pertenecen a un proyecto y son gestionadas por él.
 */
@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Task title is required")
    @Size(min = 3, max = 200, message = "Task title must be between 3 and 200 characters")
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
    
    // Constructores
    protected Task() {
        // Constructor protegido para JPA
    }
    
    /**
     * Constructor de dominio para crear una nueva tarea
     */
    public Task(String title, String description, Project project, User assignedTo) {
        this.title = validateTitle(title);
        this.description = description;
        this.project = validateProject(project);
        this.assignedTo = assignedTo;
        this.status = TaskStatus.TODO;
        this.priority = TaskPriority.MEDIUM;
    }
    
    // Métodos de negocio (Domain Methods)
    
    /**
     * Actualizar información de la tarea
     * Regla de negocio: Solo se puede actualizar si la tarea no está completada o cancelada
     */
    public void updateTask(String title, String description) {
        if (this.status == TaskStatus.COMPLETED || this.status == TaskStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update completed or cancelled task");
        }
        
        this.title = validateTitle(title);
        this.description = description;
    }
    
    /**
     * Iniciar la tarea
     * Regla de negocio: Solo se puede iniciar si está en estado TODO
     */
    public void start() {
        if (this.status != TaskStatus.TODO) {
            throw new IllegalStateException("Can only start tasks that are in TODO status");
        }
        
        this.status = TaskStatus.IN_PROGRESS;
    }
    
    /**
     * Completar la tarea
     * Regla de negocio: Solo se puede completar si está en progreso
     */
    public void complete() {
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete tasks that are in progress");
        }
        
        this.status = TaskStatus.COMPLETED;
    }
    
    /**
     * Cancelar la tarea
     * Regla de negocio: No se puede cancelar una tarea ya completada
     */
    public void cancel() {
        if (this.status == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed task");
        }
        
        this.status = TaskStatus.CANCELLED;
    }
    
    /**
     * Reabrir una tarea cancelada
     */
    public void reopen() {
        if (this.status != TaskStatus.CANCELLED) {
            throw new IllegalStateException("Can only reopen cancelled tasks");
        }
        
        this.status = TaskStatus.TODO;
    }
    
    /**
     * Asignar la tarea a un usuario
     * Regla de negocio: Solo se puede asignar si la tarea no está completada o cancelada
     */
    public void assignTo(User user) {
        if (this.status == TaskStatus.COMPLETED || this.status == TaskStatus.CANCELLED) {
            throw new IllegalStateException("Cannot assign completed or cancelled task");
        }
        
        this.assignedTo = user;
    }
    
    /**
     * Desasignar la tarea
     */
    public void unassign() {
        this.assignedTo = null;
    }
    
    /**
     * Cambiar la prioridad de la tarea
     */
    public void changePriority(TaskPriority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        
        this.priority = priority;
    }
    
    /**
     * Establecer fecha de vencimiento
     */
    public void updateDueDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
        
        this.dueDate = dueDate;
    }
    
    /**
     * Verificar si la tarea está vencida
     */
    public boolean isOverdue() {
        return dueDate != null && 
               dueDate.isBefore(LocalDateTime.now()) && 
               status != TaskStatus.COMPLETED && 
               status != TaskStatus.CANCELLED;
    }
    
    /**
     * Verificar si la tarea puede ser editada
     */
    public boolean canBeEdited() {
        return this.status != TaskStatus.COMPLETED && this.status != TaskStatus.CANCELLED;
    }
    
    /**
     * Verificar si la tarea puede ser asignada
     */
    public boolean canBeAssigned() {
        return this.status != TaskStatus.COMPLETED && this.status != TaskStatus.CANCELLED;
    }
    
    /**
     * Obtener el tiempo transcurrido desde la creación
     */
    public long getDaysSinceCreation() {
        if (createdAt == null) {
            return 0;
        }
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toDays();
    }
    
    /**
     * Obtener el tiempo restante hasta la fecha de vencimiento
     */
    public long getDaysUntilDue() {
        if (dueDate == null) {
            return -1; // No hay fecha de vencimiento
        }
        return java.time.Duration.between(LocalDateTime.now(), dueDate).toDays();
    }
    
    // Métodos de validación privados
    
    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        if (title.length() < 3 || title.length() > 200) {
            throw new IllegalArgumentException("Task title must be between 3 and 200 characters");
        }
        return title.trim();
    }
    
    private Project validateProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Task project cannot be null");
        }
        if (!project.canAcceptTasks()) {
            throw new IllegalArgumentException("Project cannot accept new tasks");
        }
        return project;
    }
    
    // Getters (solo los necesarios para el dominio)
    
    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public TaskPriority getPriority() {
        return priority;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public Project getProject() {
        return project;
    }
    
    public User getAssignedTo() {
        return assignedTo;
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
    
    protected void setTitle(String title) {
        this.title = title;
    }
    
    protected void setDescription(String description) {
        this.description = description;
    }
    
    protected void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    protected void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
    
    protected void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    protected void setProject(Project project) {
        this.project = project;
    }
    
    protected void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
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
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", assignedTo=" + (assignedTo != null ? assignedTo.getUsername() : "unassigned") +
                ", overdue=" + isOverdue() +
                '}';
    }
    
    // Enums del dominio
    
    public enum TaskStatus {
        TODO, IN_PROGRESS, COMPLETED, CANCELLED
    }
    
    public enum TaskPriority {
        LOW, MEDIUM, HIGH, URGENT
    }
}
