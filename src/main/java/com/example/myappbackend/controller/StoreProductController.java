package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Category;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.CategoriesRepository;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/store/products")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StoreProductController {

    @Autowired
    private final UserRepository userRepository;
    @Autowired private final StoreRepository storeRepository;
    @Autowired private final ProductRepository productRepository;
    @Autowired private final JwtService jwtService;
    @Autowired private final CategoriesRepository categoryRepository;

    private User getUserFromRequest(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Stores getStoreByUser(User user) {
        return storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng của người quản lý"));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/all-products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        Stores store = getStoreByUser(user);
        List<ProductResponse> products = productRepository.findByStore_StoreIdAndDeletedFalse(store.getStoreId())
                .stream()
                .map(this::convertToResponse)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/create-product")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request, HttpServletRequest httpRequest) {
        User user = getUserFromRequest(httpRequest);
        Stores store = getStoreByUser(user);
        Products product = new Products();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStock(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setStore(store);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        product.setDeleted(false);
        productRepository.save(product);
        return ResponseEntity.ok(convertToResponse(product));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id,
                                                         @RequestBody ProductRequest request,
                                                         HttpServletRequest httpRequest) {
        User user = getUserFromRequest(httpRequest);
        Stores store = getStoreByUser(user);
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (!product.getStore().getStoreId().equals(store.getStoreId()) || product.isDeleted())
            throw new RuntimeException("Không có quyền cập nhật sản phẩm");

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        productRepository.save(product);
        return ResponseEntity.ok(convertToResponse(product));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id, HttpServletRequest httpRequest) {
        User user = getUserFromRequest(httpRequest);
        Stores store = getStoreByUser(user);
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (!product.getStore().getStoreId().equals(store.getStoreId()) || product.isDeleted())
            throw new RuntimeException("Không có quyền xóa sản phẩm");

        product.setDeleted(true);
        productRepository.save(product);
        return ResponseEntity.noContent().build();
    }

    private ProductResponse convertToResponse(Products product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
