package com.example.myappbackend.repository;

import com.example.myappbackend.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findByStore_StoreId(Integer storeId);
}
