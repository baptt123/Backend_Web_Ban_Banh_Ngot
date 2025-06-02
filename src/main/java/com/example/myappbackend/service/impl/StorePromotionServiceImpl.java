package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.model.Promotions;
import com.example.myappbackend.model.StorePromotions;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.PromotionRepository;
import com.example.myappbackend.repository.StorePromotionRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.service.interfaceservice.StorePromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorePromotionServiceImpl implements StorePromotionService {

    private final PromotionRepository promotionsRepository;
    private final StorePromotionRepository storePromotionsRepository;
    private final StoreRepository storesRepository;

    @Override
    public List<PromotionResponse> getAllPromotionsForStore(Integer storeId) {
        List<StorePromotions> storePromotions = storePromotionsRepository.findByStore_StoreId(storeId);
        return storePromotions.stream()
                .map(sp -> {
                    Promotions p = sp.getPromotion();
                    PromotionResponse res = new PromotionResponse();
                    res.setPromotionId(p.getPromotionId());
                    res.setName(p.getName());
                    res.setDescription(p.getDescription());
                    res.setDiscountPercentage(p.getDiscountPercentage());
                    res.setStartDate(p.getStartDate());
                    res.setEndDate(p.getEndDate());
                    return res;
                }).collect(Collectors.toList());
    }

    @Override
    public PromotionResponse createPromotion(PromotionRequest request) {
        Promotions promotion = new Promotions();
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountPercentage(request.getDiscountPercentage());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        // mặc định createdBy là user_id = 1
        User dummyUser = new User();
        dummyUser.setUserId(1);
        promotion.setCreatedBy(dummyUser);

        Promotions saved = promotionsRepository.save(promotion);

        StorePromotions storePromotion = new StorePromotions();
        storePromotion.setStore(storesRepository.findById(1).orElseThrow());
        storePromotion.setPromotion(saved);
        storePromotionsRepository.save(storePromotion);

        PromotionResponse response = new PromotionResponse();
        response.setPromotionId(saved.getPromotionId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setDiscountPercentage(saved.getDiscountPercentage());
        response.setStartDate(saved.getStartDate());
        response.setEndDate(saved.getEndDate());
        return response;
    }

    @Override
    public void deletePromotion(Integer promotionId) {
        storePromotionsRepository.deleteAll(
                storePromotionsRepository.findAll().stream()
                        .filter(sp -> sp.getPromotion().getPromotionId().equals(promotionId))
                        .toList()
        );
        promotionsRepository.deleteById(promotionId);
    }
}
