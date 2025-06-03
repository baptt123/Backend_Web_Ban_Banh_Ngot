package com.example.myappbackend.service.interfaceservice;

import java.util.List;

public interface StoreRevenueService {
    List<RevenueResponse> getDailyRevenue(RevenueFilterRequest request);
    List<RevenueResponse> getWeeklyRevenue(Integer year);
    List<RevenueResponse> getMonthlyRevenue(Integer year);
}
