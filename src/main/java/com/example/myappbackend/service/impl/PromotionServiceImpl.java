package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Promotions;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.PromotionRepository;
import com.example.myappbackend.service.interfaceservice.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    @Override
    public PromotionResponse createPromotion(PromotionRequest request) {
        Promotions promotion = new Promotions();
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountPercentage(request.getDiscountPercentage());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        // Giả định createdBy là admin ID = 1
        User admin = new User();
        admin.setUserId(1);
        promotion.setCreatedBy(admin);
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    public PromotionResponse updatePromotion(Integer id, PromotionRequest request) {
        Promotions promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountPercentage(request.getDiscountPercentage());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    public void deletePromotion(Integer id) {
        Promotions promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        promotionRepository.delete(promotion);
    }

    @Override
    public PromotionResponse getPromotionById(Integer id) {
        Promotions promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        return toResponse(promotion);
    }

    @Override
    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private PromotionResponse toResponse(Promotions promotion) {
        PromotionResponse response = new PromotionResponse();
        response.setPromotionId(promotion.getPromotionId());
        response.setName(promotion.getName());
        response.setDescription(promotion.getDescription());
        response.setDiscountPercentage(promotion.getDiscountPercentage());
        response.setStartDate(promotion.getStartDate());
        response.setEndDate(promotion.getEndDate());
        return response;
    }
}