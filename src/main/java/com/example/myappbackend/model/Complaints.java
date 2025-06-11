package com.example.myappbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
public class Complaints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "order_id", nullable = false)
//    private Orders order;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "complaint_text", nullable = false, columnDefinition = "TEXT")
    private String complaintText;

    @Column(name = "admin_response", columnDefinition = "TEXT")
    private String adminResponse;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    private ComplaintStatus status = ComplaintStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

//    public enum ComplaintStatus {
//        PENDING, IN_PROGRESS, RESOLVED, REJECTED
//    }
}