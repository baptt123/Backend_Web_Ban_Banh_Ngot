package com.example.myappbackend.controller;

import com.example.myappbackend.dto.OrderDetailResponse;
import com.example.myappbackend.service.interfaceservice.OrderDetailService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderDetailService orderDetailService;

    public OrderController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<List<OrderDetailResponse>> getOrderHistory(@PathVariable Integer orderId) {
        List<OrderDetailResponse> details = orderDetailService.getOrderHistoryByOrderId(orderId);
        return ResponseEntity.ok(details);
    }
}
