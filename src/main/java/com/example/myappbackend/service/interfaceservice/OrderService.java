package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.OrderRequest;
import com.example.myappbackend.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequestDTO);
}
