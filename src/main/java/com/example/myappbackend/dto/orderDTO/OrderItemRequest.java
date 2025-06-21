package com.example.myappbackend.dto.orderDTO;

import lombok.Data;
import java.util.List;

@Data
public class OrderItemRequest {
    private int productId;
    private int quantity;
    private String customization;
}