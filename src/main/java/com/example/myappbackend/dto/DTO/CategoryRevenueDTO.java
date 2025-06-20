package com.example.myappbackend.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRevenueDTO {
    private String categoryName;
    private BigDecimal revenue;
    private Double percentage;
}