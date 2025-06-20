package com.example.myappbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResponseDTO {
    private Integer id;
    private Integer rating;
    private String username;
    private String fullName;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
