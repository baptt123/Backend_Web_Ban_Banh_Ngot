package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.interfaceservice.PromotionService;
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
@RequestMapping("/api/store/promotions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
@RequiredArgsConstructor
public class PromotionController {
    @Autowired
    private final PromotionService promotionService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/create-promotion")
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody PromotionRequest request, HttpServletRequest servletRequest) {
        Integer storeId = getStoreIdFromRequest(servletRequest);
        String token = Arrays.stream(servletRequest.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Access token not found"));

        PromotionResponse response = promotionService.createPromotion(request, storeId, token);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}/update-promotion")
    public ResponseEntity<PromotionResponse> updatePromotion(@PathVariable Integer id,
                                                             @RequestBody PromotionRequest request,
                                                             HttpServletRequest servletRequest) {
        PromotionResponse response = promotionService.updatePromotion(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{id}/delete-promotion")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer id, HttpServletRequest servletRequest) {
        // Nếu bạn muốn check quyền hoặc storeId thì lấy ra ở đây
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

//    @PreAuthorize("hasAuthority('MANAGER')")
//    @GetMapping("/{id}/get-promotion")
//    public ResponseEntity<PromotionResponse> getPromotionById(@PathVariable Integer id) {
//        PromotionResponse response = promotionService.getPromotionById(id);
//        return ResponseEntity.ok(response);
//    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/get-all-promotions")
    public ResponseEntity<List<PromotionResponse>> getAllPromotions(HttpServletRequest servletRequest) {
        Integer storeId = getStoreIdFromRequest(servletRequest);
        List<PromotionResponse> promotions = promotionService.getAllPromotions(storeId);
        return ResponseEntity.ok(promotions);
    }

    // Helper để lấy storeId từ token
    private Integer getStoreIdFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new RuntimeException("No cookies in request");
        }

        String token = Arrays.stream(request.getCookies())
                .filter(c -> "access_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Access token not found"));

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stores store = storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return store.getStoreId();
    }
}
