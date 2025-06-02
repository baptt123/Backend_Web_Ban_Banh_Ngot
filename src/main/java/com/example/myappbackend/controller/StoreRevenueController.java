package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.RevenueFilterRequest;
import com.example.myappbackend.dto.response.RevenueResponse;
import com.example.myappbackend.service.interfaceservice.StoreRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//this class for using in the store revenue management system
@RestController
@RequestMapping("/api/store/revenue")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StoreRevenueController {

    private final StoreRevenueService storeRevenueService;

    @PostMapping("/daily")
    public List<RevenueResponse> getDailyRevenue(@RequestBody RevenueFilterRequest request) {
        return storeRevenueService.getDailyRevenue(request);
    }

    @GetMapping("/weekly")
    public List<RevenueResponse> getWeeklyRevenue(@RequestParam int year) {
        return storeRevenueService.getWeeklyRevenue(year);
    }

    @GetMapping("/monthly")
    public List<RevenueResponse> getMonthlyRevenue(@RequestParam int year) {
        return storeRevenueService.getMonthlyRevenue(year);
    }
}
