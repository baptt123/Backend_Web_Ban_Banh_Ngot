package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.AddToCartRequest;
import com.example.myappbackend.dto.response.CartItemResponse;
import com.example.myappbackend.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    CartItemResponse getProductById(Integer productId);
    List<CartItemResponse> getCartItemsByIds(List<Integer> productIds);
    CartResponse checkProductAvailability(AddToCartRequest request);
}
