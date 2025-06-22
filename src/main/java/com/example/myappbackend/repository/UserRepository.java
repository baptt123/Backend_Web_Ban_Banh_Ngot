package com.example.myappbackend.repository;

import com.example.myappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    List<User> findByDeleted(int deleted);
    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByStore_StoreId(Integer storeId);

    List<User> findByStore_StoreIdAndDeleted(Integer storeId, Integer deleted);
}
