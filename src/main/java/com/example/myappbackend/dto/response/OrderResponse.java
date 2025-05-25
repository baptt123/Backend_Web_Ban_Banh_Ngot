package com.example.myappbackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Integer orderId;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    @Data
    public static class OrderItem {
        private Integer productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private String customization;
    }
}
