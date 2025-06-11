package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.RatingRequest;
import com.example.myappbackend.dto.response.RatingResponse;

import java.util.List;

public interface RatingService {
    RatingResponse addRating(RatingRequest request);
    List<RatingResponse> getAllRatings();
    RatingResponse updateRating(RatingRequest request);
}
