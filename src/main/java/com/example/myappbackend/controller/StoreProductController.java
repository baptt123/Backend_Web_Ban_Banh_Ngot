package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.service.interfaceservice.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/products")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class StoreProductController {

    private final StoreProductService storeProductService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(storeProductService.getAllProductsByStore(1));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeProductService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id,
                                                         @RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeProductService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        storeProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
