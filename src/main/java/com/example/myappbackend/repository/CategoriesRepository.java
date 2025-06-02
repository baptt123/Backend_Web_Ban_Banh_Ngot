package com.example.myappbackend.repository;

import com.example.myappbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStore_StoreIdAndDeleted(int storeId, int deleted);
}
