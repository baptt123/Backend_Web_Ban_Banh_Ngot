package com.example.myappbackend.dto.statisticdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class BarChartResponseDTO {
    private LocalDate date;
    private BigDecimal revenue;

}