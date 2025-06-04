package com.example.myappbackend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RevenueStatisticsResponse {
    private BigDecimal totalRevenue;
    private Integer totalOrders;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String period; // "WEEKLY", "MONTHLY", "YEARLY"
}