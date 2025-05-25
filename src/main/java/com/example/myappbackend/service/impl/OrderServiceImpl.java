package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.OrderRequest;
import com.example.myappbackend.dto.response.OrderResponse;
import com.example.myappbackend.exception.OrderNotCreateException;
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
    public OrderResponse placeOrder(OrderRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new OrderNotCreateException("Không tìm thấy người dùng với ID: " + dto.getUserId()));

        Stores store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new OrderNotCreateException("Không tìm thấy cửa hàng với ID: " + dto.getStoreId()));

        BigDecimal total = BigDecimal.ZERO;
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderRequest.OrderItemDTO item : dto.getItems()) {
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

}
