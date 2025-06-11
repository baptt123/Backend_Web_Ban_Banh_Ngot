package com.example.myappbackend.controller;

import com.example.myappbackend.dto.response.CartItemResponse;
import com.example.myappbackend.service.interfaceservice.CartService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @PostMapping("/items")
    public List<CartItemResponse> getCartItems(@RequestBody List<Integer> productIds) {
        return cartService.getCartItemsByIds(productIds);
    }
}
