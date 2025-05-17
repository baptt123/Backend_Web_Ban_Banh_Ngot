package com.example.myappbackend.controller;

import com.example.myappbackend.dto.CartItemRequest;
import com.example.myappbackend.dto.CartItemResponse;
import com.example.myappbackend.service.interfaceservice.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @PostMapping("/add")
    public String addToCart(@Valid @RequestBody CartItemRequest request) {
        cartService.addToCart(request);
        return "Added to cart.";
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Integer productId) {
        cartService.removeFromCart(productId);
        return "Removed from cart.";
    }

    @GetMapping
    public List<CartItemResponse> getCartItems() {
        return cartService.getCartItems();
    }

    @DeleteMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "Cart cleared.";
    }
}
