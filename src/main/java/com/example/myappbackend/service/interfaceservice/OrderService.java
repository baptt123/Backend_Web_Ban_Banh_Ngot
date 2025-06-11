package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.OrderRequestDTO;
import com.example.myappbackend.dto.DTO.OrderResponseDTO;
import com.example.myappbackend.dto.request.OrderRequest;
import com.example.myappbackend.dto.response.OrderHistoryResponse;
import com.example.myappbackend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequestDTO);
    List<OrderHistoryResponse> getAllOrders();
    List<OrderResponseDTO> getAllOrdersByStore();
    OrderResponseDTO getOrderById(Integer orderId);
    OrderResponseDTO createOrder(OrderRequestDTO request);
    OrderResponseDTO updateOrder(Integer orderId, OrderRequestDTO request);
    void deleteOrder(Integer orderId);
}

