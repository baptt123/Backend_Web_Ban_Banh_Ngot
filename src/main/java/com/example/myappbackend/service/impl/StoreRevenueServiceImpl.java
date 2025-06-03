package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.dto.response.RevenueStatisticsResponse;
import com.example.myappbackend.model.OrderStatus;
import com.example.myappbackend.repository.OrdersRepository;
import com.example.myappbackend.service.interfaceservice.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreRevenueServiceImpl implements RevenueService {

    private final OrdersRepository ordersRepository;

    @Override
    public RevenueStatisticsResponse getWeeklyRevenue(LocalDateTime startDate) {
        LocalDateTime endDate = startDate.plusWeeks(1);
        return calculateRevenue(startDate, endDate, "WEEKLY");
    }

    @Override
    public RevenueStatisticsResponse getMonthlyRevenue(LocalDateTime startDate) {
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return calculateRevenue(startDate, endDate, "MONTHLY");
    }

    @Override
    public RevenueStatisticsResponse getYearlyRevenue(Integer year) {
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfYear());
        return calculateRevenue(startDate, endDate, "YEARLY");
    }

    @Override
    public List<RevenueStatisticsResponse> getRevenueHistory(String period, LocalDateTime startDate, LocalDateTime endDate) {
        List<RevenueStatisticsResponse> revenueHistory = new ArrayList<>();
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            RevenueStatisticsResponse revenue;
            switch (period.toUpperCase()) {
                case "WEEKLY":
                    revenue = getWeeklyRevenue(currentDate);
                    currentDate = currentDate.plusWeeks(1);
                    break;
                case "MONTHLY":
                    revenue = getMonthlyRevenue(currentDate);
                    currentDate = currentDate.plusMonths(1);
                    break;
                case "YEARLY":
                    revenue = getYearlyRevenue(currentDate.getYear());
                    currentDate = currentDate.plusYears(1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period. Use WEEKLY, MONTHLY, or YEARLY");
            }
            revenueHistory.add(revenue);
        }
        return revenueHistory;
    }

    private RevenueStatisticsResponse calculateRevenue(LocalDateTime startDate, LocalDateTime endDate, String period) {
        List<Object[]> results = ordersRepository.calculateRevenue(startDate, endDate, OrderStatus.SHIPPED);
        
        RevenueStatisticsResponse response = new RevenueStatisticsResponse();
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        response.setPeriod(period);
        
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            response.setTotalRevenue((BigDecimal) result[0]);
            response.setTotalOrders(((Long) result[1]).intValue());
        } else {
            response.setTotalRevenue(BigDecimal.ZERO);
            response.setTotalOrders(0);
        }
        
        return response;
    }
    @Override
    public List<ProductRevenueDTO> getRevenueByProducts(LocalDateTime startDate, LocalDateTime endDate) {
        return ordersRepository.getRevenueByProducts(startDate, endDate, OrderStatus.SHIPPED);
    }
}