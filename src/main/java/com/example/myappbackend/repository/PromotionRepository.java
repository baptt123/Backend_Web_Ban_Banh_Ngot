package com.example.myappbackend.repository;

import com.example.myappbackend.model.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotions, Integer> {
    List<Promotions> findByStore_StoreIdAndDeleted(Integer storeId, Integer deleted);

}
