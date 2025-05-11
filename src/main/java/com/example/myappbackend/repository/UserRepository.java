package com.example.myappbackend.repository;

import com.example.myappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * Repository work with entity (via CRUD method inheritance from JPA Repository).
 * - Received Data from Database via Hibernate
 * - JPA auto generate SQL like: select from ,.... (findBy[fieldName], countBy[fieldName], ...)
 * - Return data --> Service for business logic
 * - Separate the business logic and data access logic
*/

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // auto gen SELECT * FROM users WHERE username = ? with JPA
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);
}