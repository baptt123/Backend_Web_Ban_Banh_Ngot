package com.example.myappbackend.repository;

import com.example.myappbackend.model.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotions, Integer> {
    Optional<Promotions> findByName(String name);

}
