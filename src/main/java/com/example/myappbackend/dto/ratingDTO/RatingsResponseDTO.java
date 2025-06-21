package com.example.myappbackend.dto.ratingDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RatingsResponseDTO {
    private String fullName;
    private String avatarUrl;
    private Integer rating;
    private LocalDateTime createdAt;
}
