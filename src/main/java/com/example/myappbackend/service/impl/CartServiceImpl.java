package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.response.CartItemResponse;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.service.interfaceservice.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    @Autowired
    private final ProductRepository productsRepository;

    @Override
    public CartItemResponse getProductById(Integer productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm với ID: " + productId));
        return mapToCartItemResponse(product);
    }

    @Override
    public List<CartItemResponse> getCartItemsByIds(List<Integer> productIds) {
        List<CartItemResponse> responseList = new ArrayList<>();
        for (Integer id : productIds) {
            responseList.add(getProductById(id));
        }
        return responseList;
    }

    private CartItemResponse mapToCartItemResponse(Products product) {
        CartItemResponse res = new CartItemResponse();
        res.setProductId(product.getProductId());
        res.setName(product.getName());
        res.setPrice(product.getPrice());
        res.setStock(product.getStock());
        res.setImageUrl(product.getImageUrl());
        res.setQuantity(1); // Mặc định 1, client sẽ điều chỉnh thêm
        return res;
    }
}
