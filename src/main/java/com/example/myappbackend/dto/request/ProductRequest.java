package com.example.myappbackend.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer categoryId;
    private Integer storeId;
    private String imageUrl;
    private boolean deleted;
}
