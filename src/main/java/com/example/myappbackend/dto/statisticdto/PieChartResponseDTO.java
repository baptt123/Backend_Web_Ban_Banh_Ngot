package com.example.myappbackend.dto.statisticdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class PieChartResponseDTO {
    private String categoryName;
    private BigDecimal revenue;

}