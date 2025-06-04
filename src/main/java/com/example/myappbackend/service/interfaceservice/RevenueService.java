package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.dto.response.RevenueStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface RevenueService {
    RevenueStatisticsResponse getWeeklyRevenue(LocalDateTime startDate);
    RevenueStatisticsResponse getMonthlyRevenue(LocalDateTime startDate);
    RevenueStatisticsResponse getYearlyRevenue(Integer year);
    List<RevenueStatisticsResponse> getRevenueHistory(String period, LocalDateTime startDate, LocalDateTime endDate);
    List<ProductRevenueDTO> getRevenueByProducts(LocalDateTime startDate, LocalDateTime endDate);
}