package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data
public class CartItemDTO {
    private Integer productId;
    private Integer storeId;
    private Integer quantity;
    private String customization;
}