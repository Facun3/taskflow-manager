package com.taskflow.repository;

import com.taskflow.entity.Project;
import com.taskflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProjectRepository - Repositorio simple para Project
 * 
 * Extiende JpaRepository que proporciona métodos CRUD básicos
 * y algunos métodos de consulta simples.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // Métodos de búsqueda por propietario
    List<Project> findByOwner(User owner);
    List<Project> findByOwnerAndStatus(User owner, Project.ProjectStatus status);
    Page<Project> findByOwner(User owner, Pageable pageable);
    Page<Project> findByOwnerAndStatus(User owner, Project.ProjectStatus status, Pageable pageable);
    
    // Métodos de búsqueda por estado
    List<Project> findByStatus(Project.ProjectStatus status);
    Page<Project> findByStatus(Project.ProjectStatus status, Pageable pageable);
    
    // Métodos de búsqueda por texto
    List<Project> findByNameContainingIgnoreCase(String name);
    List<Project> findByDescriptionContainingIgnoreCase(String description);
    
    // Métodos de conteo
    long countByOwner(User owner);
    long countByStatus(Project.ProjectStatus status);
}
