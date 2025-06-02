package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.service.interfaceservice.StorePromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/store/promotions")
@RequiredArgsConstructor
public class StorePromotionController {

    private final StorePromotionService storePromotionService;

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(storePromotionService.getAllPromotionsForStore(1));
    }

    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody PromotionRequest request) {
        return ResponseEntity.ok(storePromotionService.createPromotion(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer id) {
        storePromotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}
