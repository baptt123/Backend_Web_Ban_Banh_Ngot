package com.example.myappbackend.dto.DTO;

import com.example.myappbackend.model.OrderStatus;
import com.example.myappbackend.model.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDTO {
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private boolean deleted;
}