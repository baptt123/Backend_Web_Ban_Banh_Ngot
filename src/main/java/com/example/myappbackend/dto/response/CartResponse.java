package com.example.myappbackend.dto.response;

import lombok.Data;

@Data
public class CartResponse {
    private Integer productId;
    private String name;
    private Integer quantity;
    private String storeName;
    private Integer stock;
    private boolean available;
}
