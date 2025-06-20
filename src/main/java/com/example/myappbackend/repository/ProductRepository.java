package com.example.myappbackend.repository;

import com.example.myappbackend.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findByStore_StoreIdAndDeletedFalse(Integer storeId);
//    Optional<Products> findByProductIdAndStore_StoreId(Integer productId, Integer storeId);
    @Query("SELECT p FROM Products p " +
            "JOIN p.category c " +
            "WHERE c.deleted = 0 " +
            "AND (:storeId IS NULL OR p.store.storeId = :storeId) " +
            "AND (:categoryId IS NULL OR c.categoryId = :categoryId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Products> findProductsWithFilters(
            @Param("storeId") Integer storeId,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
//    Optional<Products> findByProductIdAndDeletedFalse(Integer productId);
    Optional<Products> findByProductIdAndStore_StoreId(Integer productId, Integer storeId);


/*
Hàm xử lí cho giỏ hàng
 */
    List<Products> findByNameContainingIgnoreCase(String name);

    List<Products> findByCategory_CategoryId(Integer categoryId);

    List<Products> findByStore_StoreId(Integer storeId);

    List<Products> findByNameContainingIgnoreCaseAndStore_StoreId(String name, Integer storeId);

    List<Products> findByNameContainingIgnoreCaseAndCategory_CategoryId(String name, Integer categoryId);
}
