package com.example.myappbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Integer userId;
    private Integer storeId;
    private String paymentMethod; // "COD" hoặc "ONLINE"
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private String customization;
    }
}

