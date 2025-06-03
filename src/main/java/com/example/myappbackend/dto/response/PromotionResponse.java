package com.example.myappbackend.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromotionResponse {
    private Integer promotionId;
    private String name;
    private String description;
    private BigDecimal discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}