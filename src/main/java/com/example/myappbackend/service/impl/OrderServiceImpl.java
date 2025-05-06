package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.OrderDetailResponse;
import com.example.myappbackend.repository.OrderRepository;
import com.example.myappbackend.service.interfaceservice.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderDetailRepository;

    public OrderServiceImpl(OrderRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetailResponse> getOrderHistoryByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream()
                .map(od -> new OrderDetailResponse(
                        od.getOrderDetailId(),
                        od.getOrderId(),
                        od.getProductId(),
                        od.getQuantity(),
                        od.getPrize(),
                        od.getCustomization()
                )).collect(Collectors.toList());
    }
}
