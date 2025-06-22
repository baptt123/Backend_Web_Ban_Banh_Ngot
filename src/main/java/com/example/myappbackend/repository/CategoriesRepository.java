package com.example.myappbackend.repository;

import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Integer> {
//    List<Category> findByStore_StoreIdAndDeleted(int storeId, int deleted);
//    List<Category> findByStore_StoreIdAndDeleted(Integer storeId, Integer deleted);
//    Optional<Category> findByCategoryIdAndStore_StoreIdAndDeleted(Integer categoryId, Integer storeId, Integer deleted);
    @Query("SELECT new com.example.myappbackend.dto.DTO.CategoryWithImageDTO(" +
            "c.categoryId, c.name, " +
            "MIN(p.imageUrl)" +
            ") " +
            "FROM Category c LEFT JOIN Products p ON p.category.categoryId = c.categoryId " +
            "WHERE c.deleted = 0 " +
            "GROUP BY c.categoryId, c.name")
    List<CategoryWithImageDTO> findCategoriesWithOneProductImage();
    List<Category> findByDeleted(int deleted);
}
