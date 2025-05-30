package com.example.myappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RevenueResponse {
    private String period; // e.g., "2025-05-30" or "2025-W22"
    private BigDecimal totalRevenue;
}
