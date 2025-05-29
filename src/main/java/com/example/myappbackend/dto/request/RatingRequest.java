package com.example.myappbackend.dto.request;

import lombok.Data;

@Data
public class RatingRequest {
    private Integer userId;
    private Integer productId;
    private Integer rating;
}
