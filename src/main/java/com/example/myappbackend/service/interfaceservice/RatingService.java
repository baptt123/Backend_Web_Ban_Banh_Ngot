package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.RatingRequestDTO;
import com.example.myappbackend.dto.RatingResponseDTO;

import java.util.List;

public interface RatingService {
    void addRating(RatingRequestDTO request, String token);
    void updateRating(RatingRequestDTO request, String token);
    List<RatingResponseDTO> getRatingsByProductId(Integer productId);
    List<RatingResponseDTO> getAllRatings();
}

