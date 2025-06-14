package com.example.myappbackend.dto.request;

import lombok.Data;

import java.util.List;
//this class for order feature
@Data
public class OrderRequest {
    private Integer userId;
    private Integer storeId;
    private String paymentMethod; // "COD" hoặc "ONLINE"
    private List<OrderItemDTO> items;
    private String address;
    private String phone;
    private String email;

    @Data
    public static class OrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private String customization;
    }
}

