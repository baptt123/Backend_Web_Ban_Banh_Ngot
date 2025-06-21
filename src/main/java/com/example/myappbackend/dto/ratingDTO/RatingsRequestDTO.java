package com.example.myappbackend.dto.ratingDTO;

import lombok.Data;

@Data
public class RatingsRequestDTO {
    private Integer productId;
    private Integer rating; // từ 1 đến 5
}
