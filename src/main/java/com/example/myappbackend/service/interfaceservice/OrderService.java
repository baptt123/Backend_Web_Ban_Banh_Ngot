package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.OrderDetailResponse;

import java.util.List;

public interface OrderService {
    List<OrderDetailResponse> getOrderHistoryByOrderId(Integer orderId);
}
