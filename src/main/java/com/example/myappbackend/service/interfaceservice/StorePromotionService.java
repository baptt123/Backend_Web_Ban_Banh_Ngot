package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;

import java.util.List;

public interface StorePromotionService {
    List<PromotionResponse> getAllPromotionsForStore(Integer storeId);
    PromotionResponse createPromotion(PromotionRequest request);
    void deletePromotion(Integer promotionId);
}
