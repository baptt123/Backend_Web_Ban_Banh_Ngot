package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.repository.CategoriesRepository;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.service.interfaceservice.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreProductServiceImpl implements StoreProductService {

    private final ProductRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final StoreRepository storesRepository;

    @Override
    public List<ProductResponse> getAllProductsByStore(Integer storeId) {
        return productsRepository.findByStore_StoreId(storeId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Products product = new Products();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        product.setStore(storesRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Store with ID 1 not found")));

        Products saved = productsRepository.save(product);
        return mapToResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(Integer productId, ProductRequest request) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        product.setStore(storesRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Store with ID 1 not found")));

        return mapToResponse(productsRepository.save(product));
    }

    @Override
    public void deleteProduct(Integer productId) {
        productsRepository.deleteById(productId);
    }

    private ProductResponse mapToResponse(Products product) {
        ProductResponse res = new ProductResponse();
        res.setProductId(product.getProductId());
        res.setName(product.getName());
        res.setDescription(product.getDescription());
        res.setPrice(product.getPrice());
        res.setStock(product.getStock());
        res.setImageUrl(product.getImageUrl());
        res.setCategoryName(product.getCategory().getName());
        res.setDeleted(0); // Mặc định là chưa xoá
        return res;
    }
}
