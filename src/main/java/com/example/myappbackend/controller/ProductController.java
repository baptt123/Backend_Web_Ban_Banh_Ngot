package com.example.myappbackend.controller;

import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.service.interfaceservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
//    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER') or hasRole('ADMIN')")
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
    @GetMapping("/getproducts")
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}

