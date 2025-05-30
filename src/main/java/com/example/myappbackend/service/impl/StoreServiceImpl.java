package com.example.myappbackend.service.impl;



import com.example.myappbackend.dto.response.OrderResponse;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.dto.response.RevenueResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Orders;
import com.example.myappbackend.model.StorePromotions;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.repository.OrdersRepository;
import com.example.myappbackend.repository.StorePromotionRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.service.interfaceservice.StoreService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final OrdersRepository ordersRepository;
    private final StorePromotionRepository promotionsRepository;

    public StoreServiceImpl(StoreRepository storeRepository, OrdersRepository ordersRepository, StorePromotionRepository promotionsRepository) {
        this.storeRepository = storeRepository;
        this.ordersRepository = ordersRepository;
        this.promotionsRepository = promotionsRepository;
    }

    private Stores getStoreById(Integer storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + storeId));
    }

    private BigDecimal calculateTotalRevenue(List<Orders> orders) {
        return orders.stream()
                .map(Orders::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<RevenueResponse> getRevenueByDay(Integer storeId) {
        Stores store = getStoreById(storeId);
        Map<LocalDate, BigDecimal> revenueMap = new HashMap<>();
        for (Orders order : ordersRepository.findByStore(store)) {
            LocalDate date = order.getCreatedAt().toLocalDate();
            revenueMap.put(date, revenueMap.getOrDefault(date, BigDecimal.ZERO).add(order.getTotalAmount()));
        }
        List<RevenueResponse> result = new ArrayList<>();
        revenueMap.forEach((day, total) -> result.add(new RevenueResponse(day.toString(), total)));
        return result;
    }

    @Override
    public List<RevenueResponse> getRevenueByWeek(Integer storeId) {
        Stores store = getStoreById(storeId);
        Map<String, BigDecimal> revenueMap = new HashMap<>();
        WeekFields weekFields = WeekFields.ISO;
        for (Orders order : ordersRepository.findByStore(store)) {
            LocalDate date = order.getCreatedAt().toLocalDate();
            String weekLabel = date.getYear() + "-W" + date.get(weekFields.weekOfWeekBasedYear());
            revenueMap.put(weekLabel, revenueMap.getOrDefault(weekLabel, BigDecimal.ZERO).add(order.getTotalAmount()));
        }
        List<RevenueResponse> result = new ArrayList<>();
        revenueMap.forEach((week, total) -> result.add(new RevenueResponse(week, total)));
        return result;
    }

    @Override
    public List<RevenueResponse> getRevenueByMonth(Integer storeId) {
        Stores store = getStoreById(storeId);
        Map<String, BigDecimal> revenueMap = new HashMap<>();
        for (Orders order : ordersRepository.findByStore(store)) {
            String month = order.getCreatedAt().getYear() + "-" + String.format("%02d", order.getCreatedAt().getMonthValue());
            revenueMap.put(month, revenueMap.getOrDefault(month, BigDecimal.ZERO).add(order.getTotalAmount()));
        }
        List<RevenueResponse> result = new ArrayList<>();
        revenueMap.forEach((month, total) -> result.add(new RevenueResponse(month, total)));
        return result;
    }

    @Override
    public List<OrderResponse> getOrdersByStore(Integer storeId) {
        Stores store = getStoreById(storeId);
        List<Orders> orders = ordersRepository.findByStore(store);
        List<OrderResponse> result = new ArrayList<>();
        for (Orders order : orders) {
            OrderResponse o = new OrderResponse();
            o.setOrderId(order.getOrderId());
            o.setTotalAmount(order.getTotalAmount());
            o.setStatus(order.getStatus().name());
            o.setPaymentMethod(order.getPaymentMethod().name());
            o.setCreatedAt(order.getCreatedAt());
            result.add(o);
        }
        return result;
    }

    @Override
    public List<PromotionResponse> getPromotionsByStore(Integer storeId) {
        Stores store = getStoreById(storeId);
        List<StorePromotions> list = promotionsRepository.findByStore(store);
        List<PromotionResponse> result = new ArrayList<>();
        for (StorePromotions sp : list) {
            PromotionResponse dto = new PromotionResponse();
            dto.setPromotionId(sp.getPromotion().getPromotionId());
            dto.setName(sp.getPromotion().getName());
            dto.setDescription(sp.getPromotion().getDescription());
            result.add(dto);
        }
        return result;
    }
}
