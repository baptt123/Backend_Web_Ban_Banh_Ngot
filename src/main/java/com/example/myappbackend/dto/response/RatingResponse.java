package com.example.myappbackend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResponse {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer rating;
    private LocalDateTime createdAt;
}
