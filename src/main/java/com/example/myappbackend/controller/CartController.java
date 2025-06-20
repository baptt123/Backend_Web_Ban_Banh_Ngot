package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CartItemDTO;
import com.example.myappbackend.dto.DTO.CartResponseDTO;
import com.example.myappbackend.dto.response.CartResponse;
import com.example.myappbackend.model.User;
import com.example.myappbackend.service.impl.CartService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin({"http://localhost:5173, http://localhost:3000"})
public class CartController {
    @Autowired
    private CartService cartService;
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    // Validate before add
    @PostMapping("/validate")
    public ResponseEntity<?> validateAndAddToCart(@RequestBody CartItemDTO item, HttpServletRequest request) {
        cartService.validateAddToCart(item, request);
        return ResponseEntity.ok("OK");
    }
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    // Get all items in cart
    @PostMapping("/get-cart")
    public ResponseEntity<List<CartResponseDTO>> getCart(@RequestBody List<CartItemDTO> cartItems) {
        List<CartResponseDTO> response = cartService.getCart(cartItems);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    // Update cart quantity
    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody CartItemDTO item, HttpServletRequest request) {
        cartService.validateAddToCart(item, request);
        return ResponseEntity.ok("Updated");
    }
//    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
//    // Remove item
//    @DeleteMapping("/remove")
//    public ResponseEntity<?> removeItem(@RequestParam Integer productId, @RequestParam Integer storeId) {
//        return ResponseEntity.ok().build();
//    }
}