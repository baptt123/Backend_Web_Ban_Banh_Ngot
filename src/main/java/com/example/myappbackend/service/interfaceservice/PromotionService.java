package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;

import java.util.List;

public interface PromotionService {
    PromotionResponse createPromotion(PromotionRequest request, Integer storeId,String token);
    PromotionResponse updatePromotion(Integer id, PromotionRequest request);
    void deletePromotion(Integer id);
    PromotionResponse getPromotionById(Integer id);
    List<PromotionResponse> getAllPromotions(Integer storeId);
}
