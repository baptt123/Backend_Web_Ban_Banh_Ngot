package com.example.myappbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;
//this class represents order details histories
@Data
public class OrderHistoryDetailResponse {
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String customization;
}
