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
    public RevenueStatisticsResponse getWeeklyRevenue(LocalDateTime startDate, int storeId) {
        LocalDateTime endDate = startDate.plusWeeks(1);
        return calculateRevenue(startDate, endDate, "WEEKLY", storeId);
    }

    @Override
    public RevenueStatisticsResponse getMonthlyRevenue(LocalDateTime startDate, int storeId) {
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return calculateRevenue(startDate, endDate, "MONTHLY", storeId);
    }

    @Override
    public RevenueStatisticsResponse getYearlyRevenue(Integer year, int storeId) {
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = startDate.with(TemporalAdjusters.lastDayOfYear());
        return calculateRevenue(startDate, endDate, "YEARLY", storeId);
    }

    @Override
    public List<RevenueStatisticsResponse> getRevenueHistory(String period, LocalDateTime startDate, LocalDateTime endDate, int storeId) {
        List<RevenueStatisticsResponse> revenueHistory = new ArrayList<>();
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            RevenueStatisticsResponse revenue;
            switch (period.toUpperCase()) {
                case "WEEKLY":
                    revenue = getWeeklyRevenue(currentDate, storeId);
                    currentDate = currentDate.plusWeeks(1);
                    break;
                case "MONTHLY":
                    revenue = getMonthlyRevenue(currentDate, storeId);
                    currentDate = currentDate.plusMonths(1);
                    break;
                case "YEARLY":
                    revenue = getYearlyRevenue(currentDate.getYear(), storeId);
                    currentDate = currentDate.plusYears(1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period. Use WEEKLY, MONTHLY, or YEARLY");
            }
            revenueHistory.add(revenue);
        }
        return revenueHistory;
    }

    private RevenueStatisticsResponse calculateRevenue(LocalDateTime startDate, LocalDateTime endDate, String period, int storeId) {
        List<Object[]> results = ordersRepository.calculateRevenue(startDate, endDate, OrderStatus.SHIPPED, storeId);

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
    public List<ProductRevenueDTO> getRevenueByProducts(LocalDateTime startDate, LocalDateTime endDate, int storeId) {
        return ordersRepository.getRevenueByProducts(startDate, endDate, OrderStatus.SHIPPED, storeId);
    }
}