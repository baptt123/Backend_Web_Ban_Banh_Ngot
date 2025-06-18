package com.example.myappbackend.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer productId;
    private Integer quantity;
    private String storeAddress;
}