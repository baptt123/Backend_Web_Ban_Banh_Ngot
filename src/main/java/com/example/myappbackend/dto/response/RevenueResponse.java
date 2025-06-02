package com.example.myappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RevenueResponse {
    private LocalDate date;
    private BigDecimal totalRevenue;
}
