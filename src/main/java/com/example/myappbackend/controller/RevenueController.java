package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.dto.response.RevenueStatisticsResponse;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.interfaceservice.RevenueService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/store/revenue")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class RevenueController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RevenueService revenueService;
    private final StoreRepository storeRepository;

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/weekly")
    public ResponseEntity<RevenueStatisticsResponse> getWeeklyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, HttpServletRequest request) {
        int storeId = getStoreIdFromRequest(request); // Assuming you want to get store ID from the request
        return ResponseEntity.ok(revenueService.getWeeklyRevenue(startDate, storeId));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/monthly")
    public ResponseEntity<RevenueStatisticsResponse> getMonthlyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, HttpServletRequest request) {
        return ResponseEntity.ok(revenueService.getMonthlyRevenue(startDate, getStoreIdFromRequest(request)));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/yearly")
    public ResponseEntity<RevenueStatisticsResponse> getYearlyRevenue(
            @RequestParam Integer year,HttpServletRequest request) {
        return ResponseEntity.ok(revenueService.getYearlyRevenue(year, getStoreIdFromRequest(request)));
    }

    @PreAuthorize("hasAuthority='MANAGER'")
    @GetMapping("/history")
    public ResponseEntity<List<RevenueStatisticsResponse>> getRevenueHistory(
            @RequestParam String period,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            HttpServletRequest request) {
        int storeId = getStoreIdFromRequest(request); // Assuming you want to get store ID from the request
        return ResponseEntity.ok(revenueService.getRevenueHistory(period, startDate, endDate, storeId));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/by-products")
    public ResponseEntity<List<ProductRevenueDTO>> getRevenueByProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            HttpServletRequest request) {
        int storeId = getStoreIdFromRequest(request); // Assuming you want to get store ID from the request
        return ResponseEntity.ok(revenueService.getRevenueByProducts(startDate, endDate, storeId));
    }

    private Integer getStoreIdFromRequest(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stores store = storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return store.getStoreId();
    }
}