package com.example.myappbackend.dto.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartResponseDTO {
    private Integer productId;
    private String productName;
    private String imageUrl;
    private Integer storeId;
    private String storeName;
    private Integer quantity;
    private Integer stock;
    private BigDecimal price;
}