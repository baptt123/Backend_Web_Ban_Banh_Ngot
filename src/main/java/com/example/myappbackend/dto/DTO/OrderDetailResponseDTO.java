package com.example.myappbackend.dto.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponseDTO {
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String customization;
}
