package com.example.myappbackend.repository;

import com.example.myappbackend.model.StorePromotions;
import com.example.myappbackend.model.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePromotionRepository extends JpaRepository<StorePromotions, Integer> {
    List<StorePromotions> findByStore(Stores store);
}
