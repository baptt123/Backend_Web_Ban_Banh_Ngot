package com.example.myappbackend.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer categoryId;
    private String imageUrl;
    private int deleted;
}
