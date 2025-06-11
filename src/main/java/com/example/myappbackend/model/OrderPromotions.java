package com.example.myappbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_promotions")
@Data
public class OrderPromotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_promotion_id")
    private Integer orderPromotionId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotions promotion;
}