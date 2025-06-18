package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.dto.response.RevenueStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface RevenueService {
    RevenueStatisticsResponse getWeeklyRevenue(LocalDateTime startDate,int storeId);
    RevenueStatisticsResponse getMonthlyRevenue(LocalDateTime startDate,int storeId);
    RevenueStatisticsResponse getYearlyRevenue(Integer year,int storeId);
    List<RevenueStatisticsResponse> getRevenueHistory(String period, LocalDateTime startDate, LocalDateTime endDate,int storeId);
    List<ProductRevenueDTO> getRevenueByProducts(LocalDateTime startDate, LocalDateTime endDate,int storeId);
}