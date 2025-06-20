package com.example.myappbackend.service.impl;


import com.example.myappbackend.dto.DTO.CategoryRevenueDTO;
import com.example.myappbackend.dto.DTO.RevenueStatisticsDTO;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;

import com.example.myappbackend.repository.OrdersRepository;

import com.example.myappbackend.service.interfaceservice.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrdersRepository orderRepository;
    private final UserService userService;

    @Override
    public List<RevenueStatisticsDTO> getRevenueStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        User currentUser = userService.getCurrentUser();
        Stores store = currentUser.getStore();

        if (store == null) {
            throw new ResourceNotFoundException("Store not found for current user");
        }

        return orderRepository.getRevenueStatistics(store.getStoreId(), startDate, endDate);
    }

    @Override
    public List<CategoryRevenueDTO> getCategoryRevenue(int year, int month) {
        User currentUser = userService.getCurrentUser();
        Stores store = currentUser.getStore();

        if (store == null) {
            throw new ResourceNotFoundException("Store not found for current user");
        }

        return orderRepository.getCategoryRevenue(store.getStoreId(), year, month);
    }
}