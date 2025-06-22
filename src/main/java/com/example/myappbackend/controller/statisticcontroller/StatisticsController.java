package com.example.myappbackend.controller.statisticcontroller;

import com.example.myappbackend.dto.statisticdto.BarChartResponseDTO;
import com.example.myappbackend.dto.statisticdto.PieChartResponseDTO;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.statisticservice.StatisticsService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private JwtService jwtUtil;
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/revenue-by-time")
    public ResponseEntity<List<BarChartResponseDTO>> getRevenueByTimeRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletRequest request) {
        String token = jwtUtil.getTokenFromCookies(request); // Extract JWT from cookies
        String username = jwtUtil.extractUsername(token); // Extract username from JWT
        List<BarChartResponseDTO> response = statisticsService.getRevenueByTimeRange(username, startDate, endDate);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/revenue-by-category")
    public ResponseEntity<List<PieChartResponseDTO>> getRevenueByCategoryForMonth(
            @RequestParam int year,
            @RequestParam int month, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromCookies(request); // Extract JWT from cookies
        String username = jwtUtil.extractUsername(token); // Extract username from JWT
        List<PieChartResponseDTO> response = statisticsService.getRevenueByCategoryForMonth(username, year, month);
        return ResponseEntity.ok(response);
    }

}