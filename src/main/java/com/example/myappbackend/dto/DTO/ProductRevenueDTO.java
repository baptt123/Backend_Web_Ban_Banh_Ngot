package com.example.myappbackend.dto.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRevenueDTO {
    private String category;
    private Integer weekOfMonth;
    private BigDecimal revenue;
}