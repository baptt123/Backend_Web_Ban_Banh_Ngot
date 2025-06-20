package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.response.ProductDetailsResponse;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Products createProduct(Products product);
    Products updateProduct(Integer id, Products product);
    void deleteProduct(Integer id);
    Optional<Products> getProductById(Integer id);
    List<ProductResponse> getAllProducts();
    Page<ProductResponse> getProducts(Integer storeId, Integer categoryId,
                                      BigDecimal minPrice, BigDecimal maxPrice,Pageable pageable);
    ProductDetailsResponse getProductDetail(Integer productId);
}
