package com.example.myappbackend.service.interfaceservice;



import com.example.myappbackend.dto.response.OrderResponse;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.dto.response.RevenueResponse;

import java.util.List;

public interface StoreService {
    List<RevenueResponse> getRevenueByDay(Integer storeId);
    List<RevenueResponse> getRevenueByWeek(Integer storeId);
    List<RevenueResponse> getRevenueByMonth(Integer storeId);
    List<OrderResponse> getOrdersByStore(Integer storeId);
    List<PromotionResponse> getPromotionsByStore(Integer storeId);
}
