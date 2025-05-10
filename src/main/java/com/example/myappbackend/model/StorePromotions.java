package com.example.myappbackend.model;

import com.example.myappbackend.model.Promotions;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "store_promotions")
@Data
public class StorePromotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_promotion_id")
    private Integer storePromotionId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotions promotion;
}