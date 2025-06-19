package com.example.myappbackend.repository;

import com.example.myappbackend.model.Promotions;
import com.example.myappbackend.model.StorePromotions;
import com.example.myappbackend.model.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePromotionRepository extends JpaRepository<StorePromotions, Integer> {
    List<StorePromotions> findByStore_StoreId(Integer storeId);
//    List<StorePromotions> findByPromotion_PromotionId(Integer promotionId);
@Query("SELECT sp FROM StorePromotions sp WHERE sp.promotion.promotionId = :promotionId")
List<StorePromotions> findStorePromotionsByPromotionId(@Param("promotionId") Integer promotionId);
}
