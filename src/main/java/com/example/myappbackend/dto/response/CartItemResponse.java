package com.example.myappbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Integer productId;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private Integer stock;
}
