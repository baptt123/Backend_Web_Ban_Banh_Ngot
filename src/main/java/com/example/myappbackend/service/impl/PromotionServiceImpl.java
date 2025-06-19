package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.PromotionRequest;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Promotions;
import com.example.myappbackend.model.StorePromotions;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.PromotionRepository;
import com.example.myappbackend.repository.StorePromotionRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.interfaceservice.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final StoreRepository storeRepository;
    private final StorePromotionRepository storePromotionsRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public PromotionResponse createPromotion(PromotionRequest request, Integer storeId, String token) {
        Stores store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Promotions promotion = new Promotions();
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setDiscountPercentage(request.getDiscountPercentage());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setCreatedBy(user);

        Promotions savedPromotion = promotionRepository.save(promotion);

        StorePromotions link = new StorePromotions();
        link.setStore(store);
        link.setPromotion(savedPromotion);
        storePromotionsRepository.save(link);

        return toResponse(savedPromotion);
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

        // Cách đơn giản: xóa record liên kết (StorePromotions) nếu cần
        List<StorePromotions> links = storePromotionsRepository.findStorePromotionsByPromotionId(id);
        storePromotionsRepository.deleteAll(links);

        promotionRepository.delete(promotion);
    }

    @Override
    public PromotionResponse getPromotionById(Integer id) {
        Promotions promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        return toResponse(promotion);
    }

    @Override
    public List<PromotionResponse> getAllPromotions(Integer storeId) {
        List<StorePromotions> storePromotionsList = storePromotionsRepository.findByStore_StoreId(storeId);
        List<PromotionResponse> responseList = new ArrayList<>();

        for (StorePromotions sp : storePromotionsList) {
            Promotions promotion = sp.getPromotion();

            // Kiểm tra trạng thái deleted nếu có field này
            if (promotion.isDeleted()) {
                PromotionResponse response = new PromotionResponse();
                response.setPromotionId(promotion.getPromotionId());
                response.setName(promotion.getName());
                response.setDescription(promotion.getDescription());
                response.setDiscountPercentage(promotion.getDiscountPercentage());
                response.setStartDate(promotion.getStartDate());
                response.setEndDate(promotion.getEndDate());

                responseList.add(response);
            }
        }

        return responseList;
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
