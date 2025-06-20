package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.CategoryRequest;
import com.example.myappbackend.dto.response.CategoryResponse;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.interfaceservice.StoreCategoryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/store/categories")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class StoreCategoryController {

    private final StoreCategoryService storeCategoryService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all-categories")
    public ResponseEntity<List<CategoryResponse>> getAll(HttpServletRequest request) {
        Integer storeId = getStoreIdFromRequest(request);
        return ResponseEntity.ok(storeCategoryService.getAllCategories());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create-category")
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(storeCategoryService.createCategory(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/update-category")
    public ResponseEntity<CategoryResponse> update(@PathVariable Integer id,
                                                   @RequestBody CategoryRequest request
                                                  ) {
        return ResponseEntity.ok(storeCategoryService.updateCategory(id, request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/delete-category")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        storeCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private Integer getStoreIdFromRequest(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stores store = storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return store.getStoreId();
    }
}

