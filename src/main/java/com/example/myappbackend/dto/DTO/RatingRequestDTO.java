package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data
public class RatingRequestDTO {
    private Integer productId;
    private Integer rating;  // 1 -> 5
}
