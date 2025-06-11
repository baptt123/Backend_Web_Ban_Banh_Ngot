package com.example.myappbackend.repository;

import com.example.myappbackend.model.Ratings;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Ratings, Integer> {
    @Query("SELECT r FROM Ratings r WHERE r.product.productId = :productId AND r.user.userId = :userId")
    Optional<Ratings> findByProductIdAndUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);
}
