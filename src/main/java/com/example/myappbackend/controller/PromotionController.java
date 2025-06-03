// Controller
package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.service.interfaceservice.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping("/create-promotion")
    public ResponseEntity<PromotionResponse> create(@RequestBody PromotionRequest request) {
        return ResponseEntity.ok(promotionService.createPromotion(request));
    }

    @PutMapping("/{id}/update-promotion")
    public ResponseEntity<PromotionResponse> update(@PathVariable Integer id, @RequestBody PromotionRequest request) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, request));
    }

    @DeleteMapping("/{id}/delete-promotion")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/get-promotion")
    public ResponseEntity<PromotionResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @GetMapping("/get-all-promotions")
    public ResponseEntity<List<PromotionResponse>> getAll() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }
}