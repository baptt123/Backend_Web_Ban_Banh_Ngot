package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductDetailsResponse;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Category;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.repository.CategoriesRepository;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.service.interfaceservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Products product = new Products();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setDeleted(false);

        Category category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm"));
        product.setCategory(category);

        if (request.getStoreId() != null) {
            Stores store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng"));
            product.setStore(store);
        }

        Products saved = productRepository.save(product);

        ProductResponse response = new ProductResponse();
        response.setProductId(saved.getProductId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setPrice(saved.getPrice());
        response.setStock(saved.getStock());
        response.setImageUrl(saved.getImageUrl());
        response.setCategoryName(saved.getCategory().getName());
        response.setStoreName(saved.getStore() != null ? saved.getStore().getName() : null);

        return response;
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
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + id));

        product.setDeleted(true);
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
                dto.setCategoryId(product.getCategory().getCategoryId());
                dto.setStoreId(product.getStore().getStoreId());
                dto.setStoreName(product.getStore().getName());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Lỗi khi lấy danh sách sản phẩm.");
        }
    }

    @Override
    public Page<ProductResponse> getProducts(Integer storeId, Integer categoryId,
                                             BigDecimal minPrice, BigDecimal maxPrice,Pageable pageable) {
        Page<Products> products = productRepository.findProductsWithFilters(storeId, categoryId, minPrice, maxPrice, pageable);
        return products.map(this::convertToDto);
    }

    @Override
    public ProductDetailsResponse getProductDetail(Integer productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDetailsResponse response = new ProductDetailsResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setCategoryName(product.getCategory().getName());
        response.setStoreName(product.getStore() != null ? product.getStore().getName() : "N/A");

        return response;
    }

    // Convert entity -> DTO
    private ProductResponse convertToDto(Products product) {
        ProductResponse dto = new ProductResponse();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
            dto.setCategoryId(product.getCategory().getCategoryId());
            dto.setDeleted(product.getCategory().getDeleted());
        } else {
            dto.setCategoryName("");
            dto.setDeleted(0);
        }
        if (product.getStore() != null) {
            dto.setStoreId(product.getStore().getStoreId());     // ✅ thêm dòng này
            dto.setStoreName(product.getStore().getName());      // ✅ và dòng này
        } else {
            dto.setStoreId(null);
            dto.setStoreName("Không rõ");
        }
        return dto;
    }

}
