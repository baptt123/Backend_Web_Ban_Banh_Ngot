package com.example.myappbackend.repository;

import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStore_StoreIdAndDeleted(int storeId, int deleted);

    @Query("SELECT new com.example.myappbackend.dto.DTO.CategoryWithImageDTO(" +
            "c.categoryId, c.name, " +
            "MIN(p.imageUrl)" +
            ") " +
            "FROM Category c LEFT JOIN Products p ON p.category.categoryId = c.categoryId " +
            "WHERE c.deleted = 0 " +
            "GROUP BY c.categoryId, c.name")
    List<CategoryWithImageDTO> findCategoriesWithOneProductImage();



}
