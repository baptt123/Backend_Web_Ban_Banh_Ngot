package com.example.myappbackend.repository;

import com.example.myappbackend.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findByStore_StoreId(Integer storeId);
    List<Products> findByStore_StoreIdAndDeletedFalse(Integer storeId);
    Optional<Products> findByProductIdAndStore_StoreId(Integer productId, Integer storeId);
}
