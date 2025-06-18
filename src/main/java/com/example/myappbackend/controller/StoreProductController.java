package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.dto.response.StoreResponse;
import com.example.myappbackend.service.interfaceservice.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/products")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class StoreProductController {

    private final StoreProductService storeProductService;
    @PreAuthorize("MANAGER")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(storeProductService.getAllProductsByStore(1));
    }
    @PreAuthorize("MANAGER")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeProductService.createProduct(request));
    }
    @PreAuthorize("MANAGER")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id,
                                                         @RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeProductService.updateProduct(id, request));
    }
    @PreAuthorize("MANAGER")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        storeProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
