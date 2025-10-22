package com.taskflow.repository;

import com.taskflow.entity.Project;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TaskRepository - Repositorio simple para Task
 * 
 * Extiende JpaRepository que proporciona métodos CRUD básicos
 * y algunos métodos de consulta simples.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Métodos de búsqueda por proyecto
    List<Task> findByProject(Project project);
    List<Task> findByProjectAndStatus(Project project, Task.TaskStatus status);
    Page<Task> findByProject(Project project, Pageable pageable);
    Page<Task> findByProjectAndStatus(Project project, Task.TaskStatus status, Pageable pageable);
    
    // Métodos de búsqueda por asignado
    List<Task> findByAssignedTo(User assignedTo);
    List<Task> findByAssignedToAndStatus(User assignedTo, Task.TaskStatus status);
    Page<Task> findByAssignedTo(User assignedTo, Pageable pageable);
    Page<Task> findByAssignedToAndStatus(User assignedTo, Task.TaskStatus status, Pageable pageable);
    
    // Métodos de búsqueda por estado
    List<Task> findByStatus(Task.TaskStatus status);
    Page<Task> findByStatus(Task.TaskStatus status, Pageable pageable);
    
    // Métodos de búsqueda por prioridad
    List<Task> findByPriority(Task.TaskPriority priority);
    Page<Task> findByPriority(Task.TaskPriority priority, Pageable pageable);
    
    // Métodos de búsqueda por fechas
    List<Task> findByDueDateBefore(LocalDateTime date);
    List<Task> findByDueDateAfter(LocalDateTime date);
    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end);
    
    // Métodos de búsqueda por texto
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByDescriptionContainingIgnoreCase(String description);
    
    // Métodos de conteo
    long countByProject(Project project);
    long countByStatus(Task.TaskStatus status);
    long countByAssignedTo(User assignedTo);
}
