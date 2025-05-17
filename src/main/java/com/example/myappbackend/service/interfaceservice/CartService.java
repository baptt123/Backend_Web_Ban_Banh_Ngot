package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.CartItemRequest;
import com.example.myappbackend.dto.CartItemResponse;

import java.util.List;

public interface CartService {
    void addToCart(CartItemRequest request);
    void removeFromCart(Integer productId);
    List<CartItemResponse> getCartItems();
    void clearCart();
}
