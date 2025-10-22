package com.taskflow.repository;

import com.taskflow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository - Repositorio simple para User
 * 
 * Extiende JpaRepository que proporciona métodos CRUD básicos
 * y algunos métodos de consulta simples.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Métodos de búsqueda básicos
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Métodos de búsqueda por estado
    List<User> findByStatus(User.UserStatus status);
    Page<User> findByStatus(User.UserStatus status, Pageable pageable);
    
    // Métodos de búsqueda por texto
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    // Métodos de conteo
    long countByStatus(User.UserStatus status);
}
