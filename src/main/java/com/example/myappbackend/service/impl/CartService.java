package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.CartItemDTO;
import com.example.myappbackend.dto.DTO.CartResponseDTO;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.StoreRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private ProductRepository productsRepo;
    @Autowired
    private StoreRepository storesRepo;

    public void validateAddToCart(CartItemDTO item, HttpServletRequest request) {
        // Check JWT
        String token = getJwtFromRequest(request);
        if (token == null) {
            throw new BadCredentialsException("Chưa đăng nhập");
        }
        // Check product & stock & store
        Products product = productsRepo.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        if (product.isDeleted()) throw new RuntimeException("Sản phẩm đã bị xóa");

        if (item.getStoreId() == null || !product.getStore().getStoreId().equals(item.getStoreId())) {
            throw new RuntimeException("Sai cửa hàng");
        }
        if (item.getQuantity() > product.getStock()) {
            throw new RuntimeException("Không đủ hàng trong kho");
        }
    }

    public List<CartResponseDTO> getCart(List<CartItemDTO> cartItems) {
        List<CartResponseDTO> result = new ArrayList<>();
        if (cartItems == null) return result;

        for (CartItemDTO item : cartItems) {
            Integer productId = item.getProductId();
            Integer storeId = item.getStoreId();
            Integer quantity = item.getQuantity();

            Products product = productsRepo.findById(productId).orElse(null);
            if (product == null) continue;

            Stores store = product.getStore();
            if (store == null || !store.getStoreId().equals(storeId)) continue;

            CartResponseDTO dto = new CartResponseDTO();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getName());
            dto.setImageUrl(product.getImageUrl());
            dto.setStoreId(store.getStoreId());
            dto.setStoreName(store.getName());
            dto.setQuantity(quantity); // ✅ Gán lại quantity từ cartItems
            dto.setStock(product.getStock());
            dto.setPrice(product.getPrice());

            result.add(dto);
        }

        return result;
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        // Lấy JWT từ cookie tên "access_token"
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}