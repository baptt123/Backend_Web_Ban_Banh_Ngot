package com.example.myappbackend.dto.response;

import lombok.Data;

@Data
public class PromotionResponse {
    private Integer promotionId;
    private String name;
    private String description;
}
