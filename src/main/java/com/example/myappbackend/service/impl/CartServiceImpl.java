package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.CartItemRequest;
import com.example.myappbackend.dto.CartItemResponse;
import com.example.myappbackend.service.interfaceservice.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final List<CartItemResponse> cartItems = new ArrayList<>();

    @Override
    public void addToCart(CartItemRequest request) {
        for (CartItemResponse item : cartItems) {
            if (item.getProductId().equals(request.getProductId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                return;
            }
        }
        cartItems.add(new CartItemResponse(
                request.getProductId(),
                request.getName(),
                request.getPrice(),
                request.getQuantity(),
                request.getImageUrl()
        ));
    }

    @Override
    public void removeFromCart(Integer productId) {
        cartItems.removeIf(item -> item.getProductId().equals(productId));
    }

    @Override
    public List<CartItemResponse> getCartItems() {
        return new ArrayList<>(cartItems); // tránh trả reference gốc
    }

    @Override
    public void clearCart() {
        cartItems.clear();
    }
}
