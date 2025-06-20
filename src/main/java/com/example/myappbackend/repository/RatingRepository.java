package com.example.myappbackend.repository;

import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Ratings;
import com.example.myappbackend.model.User;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Ratings, Integer> {
    List<Ratings> findByProduct(Products product);
    List<Ratings> findAll();
    Optional<Ratings> findByProductAndUser(Products product, User user);
}
