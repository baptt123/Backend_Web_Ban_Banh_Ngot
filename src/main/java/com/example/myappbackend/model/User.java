//package com.example.myappbackend.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Integer userId;
//
//    @NotBlank(message = "Username không được để trống")
//    @Size(min = 4, max = 50, message = "Username phải từ 4-50 ký tự")
//    @Column(nullable = false, unique = true)
//    private String username;
//
//    @NotBlank(message = "Password không được để trống")
//    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
//    @Column(nullable = false)
//    private String password;
//
//    @NotBlank(message = "Email không được để trống")
//    @Email(message = "Email không đúng định dạng")
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Pattern(regexp = "^[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
//    private String phone;
//
//    @NotNull(message = "Role không được để trống")
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//        if (this.role == null) {
//            this.role = Role.USER;
//        }
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//}
package com.example.myappbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}