package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.CategoryRevenueDTO;
import com.example.myappbackend.dto.DTO.RevenueStatisticsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    List<RevenueStatisticsDTO> getRevenueStatistics(LocalDateTime startDate, LocalDateTime endDate);
    List<CategoryRevenueDTO> getCategoryRevenue(int year, int month);
}