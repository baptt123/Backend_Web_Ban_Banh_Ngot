package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.OrderResponseDTO;
import com.example.myappbackend.dto.request.OrderRequest;
import com.example.myappbackend.dto.response.OrderHistoryResponse;
import com.example.myappbackend.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequestDTO);
    List<OrderHistoryResponse> getAllOrders();
    List<OrderResponseDTO> getOrdersByStoreId(Integer storeId);
    OrderResponseDTO getOrderById(Integer orderId);
}
