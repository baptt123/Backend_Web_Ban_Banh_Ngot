package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.RatingRequest;
import com.example.myappbackend.dto.response.RatingResponse;
import com.example.myappbackend.service.interfaceservice.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/addratings")
    public RatingResponse create(@RequestBody RatingRequest request) {
        return ratingService.addRating(request);
    }

    @GetMapping("/getratings")
    public List<RatingResponse> getAll() {
        return ratingService.getAllRatings();
    }
    @PutMapping("/updateratings")
    public RatingResponse update(@RequestBody RatingRequest request) {
        return ratingService.updateRating(request);
    }

}
