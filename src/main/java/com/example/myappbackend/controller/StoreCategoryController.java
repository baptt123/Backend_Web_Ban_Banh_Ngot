package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.CategoryRequest;
import com.example.myappbackend.dto.response.CategoryResponse;
import com.example.myappbackend.service.interfaceservice.StoreCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/categories")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StoreCategoryController {

    private final StoreCategoryService storeCategoryService;

    public StoreCategoryController(StoreCategoryService storeCategoryService) {
        this.storeCategoryService = storeCategoryService;
    }
    @PreAuthorize("MANAGER")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(storeCategoryService.getAllCategoriesByStore(1));
    }
    @PreAuthorize("MANAGER")
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(storeCategoryService.createCategory(request));
    }
    @PreAuthorize("MANAGER")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Integer id,
                                                   @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(storeCategoryService.updateCategory(id, request));
    }
    @PreAuthorize("MANAGER")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        storeCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
