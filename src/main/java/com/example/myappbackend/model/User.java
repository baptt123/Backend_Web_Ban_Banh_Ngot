package com.example.myappbackend.model;

import jakarta.persistence.*;
// Lombok auto generator ....
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Working with local date time
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    // Define user_id is primary key of this table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, unique = true, length = 100)
    private String userName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Define enum type for role
    public enum Role {
        customer,
        admin,
        manager,
    }

    // Set create and update time for first record when user register
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Set update time when update action have invoked
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
