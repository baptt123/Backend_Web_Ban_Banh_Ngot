package com.example.myappbackend.repository;

import com.example.myappbackend.model.StorePromotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePromotionRepository extends JpaRepository<StorePromotions, Integer> {
}
