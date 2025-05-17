package com.example.myappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
}
