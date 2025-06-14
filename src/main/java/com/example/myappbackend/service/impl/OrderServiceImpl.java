package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.OrderDetailResponseDTO;
import com.example.myappbackend.dto.DTO.OrderRequestDTO;
import com.example.myappbackend.dto.DTO.OrderResponseDTO;
import com.example.myappbackend.dto.request.OrderRequest;

import com.example.myappbackend.dto.response.*;

import com.example.myappbackend.exception.OrderNotCreateException;

import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.*;
import com.example.myappbackend.repository.*;
import com.example.myappbackend.service.interfaceservice.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    @Override
    public OrderResponse placeOrder(OrderRequest  orderRequestDTO) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new OrderNotCreateException("Không tìm thấy người dùng với ID: " + orderRequestDTO.getUserId()));

        Stores store = storeRepository.findById(orderRequestDTO.getStoreId())
                .orElseThrow(() -> new OrderNotCreateException("Không tìm thấy cửa hàng với ID: " + orderRequestDTO.getStoreId()));

        BigDecimal total = BigDecimal.ZERO;
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderRequest.OrderItemDTO item :orderRequestDTO.getItems()) {
            Products product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new OrderNotCreateException("Không tìm thấy sản phẩm với ID: " + item.getProductId()));

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            OrderDetails detail = new OrderDetails();
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice());
            detail.setCustomization(item.getCustomization());
            orderDetailsList.add(detail);
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setStore(store);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.COD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setAddress(orderRequestDTO.getAddress());
        order.setPhone(orderRequestDTO.getPhone());
        order.setEmail(orderRequestDTO.getEmail());
        Orders savedOrder = ordersRepository.save(order);
        if (savedOrder == null || savedOrder.getOrderId() == null) {
            throw new OrderNotCreateException("Không thể lưu đơn hàng.");
        }

        for (OrderDetails detail : orderDetailsList) {
            detail.setOrder(savedOrder);
            orderDetailsRepository.save(detail);
        }

        // Tạo OrderResponse
        OrderResponse response = new OrderResponse();
        response.setOrderId(savedOrder.getOrderId());
        response.setTotalAmount(savedOrder.getTotalAmount());
        response.setStatus(savedOrder.getStatus().name());
        response.setPaymentMethod(savedOrder.getPaymentMethod().name());
        response.setCreatedAt(savedOrder.getCreatedAt());

        List<OrderResponse.OrderItem> itemList = new ArrayList<>();
        for (OrderDetails detail : orderDetailsList) {
            OrderResponse.OrderItem itemRes = new OrderResponse.OrderItem();
            itemRes.setProductId(detail.getProduct().getProductId());
            itemRes.setProductName(detail.getProduct().getName());
            itemRes.setQuantity(detail.getQuantity());
            itemRes.setPrice(detail.getPrice());
            itemRes.setCustomization(detail.getCustomization());
            itemList.add(itemRes);
        }
        response.setItems(itemList);
        return response;
    }

    @Override
    public List<OrderHistoryResponse> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();

        return orders.stream().map(order -> {
            OrderHistoryResponse response = new OrderHistoryResponse();
            response.setOrderId(order.getOrderId());
            response.setStoreName(order.getStore().getName());
            response.setTotalAmount(order.getTotalAmount());
            response.setStatus(order.getStatus().name());
            response.setPaymentMethod(order.getPaymentMethod().name());
            response.setCreatedAt(order.getCreatedAt());
            response.setUpdatedAt(order.getUpdatedAt());

            List<OrderDetails> orderDetails = orderDetailsRepository.findByOrder(order);
            List<OrderHistoryDetailResponse> detailResponses = orderDetails.stream().map(detail -> {
                OrderHistoryDetailResponse d = new OrderHistoryDetailResponse();
                d.setProductName(detail.getProduct().getName());
                d.setQuantity(detail.getQuantity());
                d.setPrice(detail.getPrice());
                d.setCustomization(detail.getCustomization());
                return d;
            }).collect(Collectors.toList());

            response.setOrderDetails(detailResponses);

            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getAllOrdersByStore() {
        return ordersRepository.findByStoreStoreId(1).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Integer orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToResponse(order);
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Orders order = new Orders();
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus());
        order.setPaymentMethod(request.getPaymentMethod());
        Stores store = new Stores();
        store.setStoreId(1);
        order.setStore(store);
        ordersRepository.save(order);
        return mapToResponse(order);
    }

    @Override
    public OrderResponseDTO updateOrder(Integer orderId, OrderRequestDTO request) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus());
        order.setPaymentMethod(request.getPaymentMethod());
        ordersRepository.save(order);
        return mapToResponse(order);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        ordersRepository.delete(order);
    }

    private OrderResponseDTO mapToResponse(Orders order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setOrderDetails(order.getOrderDetails().stream().map(detail -> {
            OrderDetailResponseDTO d = new OrderDetailResponseDTO();
            d.setProductId(detail.getProduct().getProductId());
            d.setProductName(detail.getProduct().getName());
            d.setQuantity(detail.getQuantity());
            d.setPrice(detail.getPrice());
            d.setCustomization(detail.getCustomization());
            return d;
        }).collect(Collectors.toList()));
        return response;
    }

}

