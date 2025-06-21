package com.example.myappbackend.dto.orderDTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String address;
    private String phone;
    private String email;
    private String paymentMethod;
    private String promotionCode;
    private List<OrderItemRequest> items;
}