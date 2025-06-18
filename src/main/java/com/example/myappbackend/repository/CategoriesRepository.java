package com.example.myappbackend.repository;

import com.example.myappbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Integer> {
//    List<Category> findByStore_StoreIdAndDeleted(int storeId, int deleted);
    List<Category> findByStore_StoreIdAndDeleted(Integer storeId, Integer deleted);
    Optional<Category> findByCategoryIdAndStore_StoreIdAndDeleted(Integer categoryId, Integer storeId, Integer deleted);
}
