package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {
    CartItemResponse getProductById(Integer productId);
    List<CartItemResponse> getCartItemsByIds(List<Integer> productIds);
}
