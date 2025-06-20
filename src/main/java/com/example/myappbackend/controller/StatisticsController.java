package com.example.myappbackend.controller;


import com.example.myappbackend.dto.DTO.CategoryRevenueDTO;
import com.example.myappbackend.dto.DTO.RevenueStatisticsDTO;
import com.example.myappbackend.service.interfaceservice.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueStatisticsDTO>> getRevenueStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(statisticsService.getRevenueStatistics(startDate, endDate));
    }

    @GetMapping("/category-revenue")
    public ResponseEntity<List<CategoryRevenueDTO>> getCategoryRevenue(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(statisticsService.getCategoryRevenue(year, month));
    }
}