package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.service.interfaceservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Products createProduct(Products product) {
        return productRepository.save(product);
    }

    @Override
    public Products updateProduct(Integer id, Products updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setUpdatedAt(updatedProduct.getUpdatedAt());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Products> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        try {
            List<Products> products = productRepository.findAll();
            return products.stream().map(product -> {
                ProductResponse dto = new ProductResponse();
                dto.setProductId(product.getProductId());
                dto.setName(product.getName());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setStock(product.getStock());
                dto.setImageUrl(product.getImageUrl());
                dto.setCategoryName(product.getCategory().getName());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Lỗi khi lấy danh sách sản phẩm.");
        }
    }

}
