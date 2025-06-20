package com.example.myappbackend.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatisticsDTO {
    private LocalDateTime date;
    private BigDecimal revenue;
}