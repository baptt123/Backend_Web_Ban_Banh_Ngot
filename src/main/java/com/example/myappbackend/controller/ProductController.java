package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.dto.response.ProductDetailsResponse;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.service.interfaceservice.CategoriesService;
import com.example.myappbackend.service.interfaceservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoriesService categoriesService;

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

    @GetMapping
    public Page<ProductResponse> getProducts(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,       // trang hiện tại (bắt đầu từ 0)
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProducts(storeId, categoryId, minPrice, maxPrice,pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponse> getProductDetail(@PathVariable Integer id) {
        ProductDetailsResponse response = productService.getProductDetail(id);
        return ResponseEntity.ok(response);
    }


}

