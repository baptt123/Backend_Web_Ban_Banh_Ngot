package com.example.myappbackend.controller;



import com.example.myappbackend.dto.response.OrderResponse;
import com.example.myappbackend.dto.response.PromotionResponse;
import com.example.myappbackend.dto.response.RevenueResponse;
import com.example.myappbackend.service.interfaceservice.StoreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeId}/revenue/day")
    @PreAuthorize("hasRole('MANAGER')")
    public List<RevenueResponse> getRevenueByDay(@PathVariable Integer storeId) {
        return storeService.getRevenueByDay(storeId);
    }

    @GetMapping("/{storeId}/revenue/week")
    @PreAuthorize("hasRole('MANAGER')")
    public List<RevenueResponse> getRevenueByWeek(@PathVariable Integer storeId) {
        return storeService.getRevenueByWeek(storeId);
    }

    @GetMapping("/{storeId}/revenue/month")
    @PreAuthorize("hasRole('MANAGER')")
    public List<RevenueResponse> getRevenueByMonth(@PathVariable Integer storeId) {
        return storeService.getRevenueByMonth(storeId);
    }

    @GetMapping("/{storeId}/orders")
    @PreAuthorize("hasRole('MANAGER')")
    public List<OrderResponse> getOrders(@PathVariable Integer storeId) {
        return storeService.getOrdersByStore(storeId);
    }

    @GetMapping("/{storeId}/promotions")
    @PreAuthorize("hasRole('MANAGER')")
    public List<PromotionResponse> getPromotions(@PathVariable Integer storeId) {
        return storeService.getPromotionsByStore(storeId);
    }
}
