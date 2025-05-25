package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.OrderRequest;
import com.example.myappbackend.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequestDTO);
}
