package com.cupabakery.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllProducts() {
        // To do...
        return ResponseEntity.ok("Danh sách sản phẩm");
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> createProduct() {
        // To do...
        return ResponseEntity.ok("Sản phẩm đã được tạo");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct() {
        // To do...
        return ResponseEntity.ok("Sản phẩm đã được xóa");
    }
}

