package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.RevenueFilterRequest;
import com.example.myappbackend.dto.response.RevenueResponse;

import java.util.List;

public interface StoreRevenueService {
    List<RevenueResponse> getDailyRevenue(RevenueFilterRequest request);
    List<RevenueResponse> getWeeklyRevenue(Integer year);
    List<RevenueResponse> getMonthlyRevenue(Integer year);
}
