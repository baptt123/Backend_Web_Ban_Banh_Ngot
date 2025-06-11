package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.dto.response.RevenueStatisticsResponse;
import com.example.myappbackend.service.interfaceservice.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/store/revenue")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/weekly")
    public ResponseEntity<RevenueStatisticsResponse> getWeeklyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        return ResponseEntity.ok(revenueService.getWeeklyRevenue(startDate));
    }

    @GetMapping("/monthly")
    public ResponseEntity<RevenueStatisticsResponse> getMonthlyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        return ResponseEntity.ok(revenueService.getMonthlyRevenue(startDate));
    }

    @GetMapping("/yearly")
    public ResponseEntity<RevenueStatisticsResponse> getYearlyRevenue(
            @RequestParam Integer year) {
        return ResponseEntity.ok(revenueService.getYearlyRevenue(year));
    }

    @GetMapping("/history")
    public ResponseEntity<List<RevenueStatisticsResponse>> getRevenueHistory(
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(revenueService.getRevenueHistory(period, startDate, endDate));
    }
    @GetMapping("/by-products")
    public ResponseEntity<List<ProductRevenueDTO>> getRevenueByProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(revenueService.getRevenueByProducts(startDate, endDate));
    }
}