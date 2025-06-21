package com.example.myappbackend.controller.ratingcontroller;

import com.example.myappbackend.dto.ratingDTO.RatingsRequestDTO;
import com.example.myappbackend.dto.ratingDTO.RatingsResponseDTO;
import com.example.myappbackend.model.User;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.ratingservice.RatingsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin({"http://localhost:3000", "http://localhost:5173"})
public class RatingHandlingController {
    @Autowired
    private final RatingsService ratingsService;
    @Autowired
    private final JwtService jwtService;

    public RatingHandlingController(RatingsService ratingsService, JwtService jwtService) {
        this.ratingsService = ratingsService;
        this.jwtService = jwtService;
    }
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<String> addRating(@RequestBody RatingsRequestDTO dto, HttpServletRequest request) {
        String token= jwtService.getTokenFromCookies(request);
        User userToken = jwtService.extractUserFromToken(token);
        ratingsService.addRating(dto, userToken);
        return ResponseEntity.ok("Đánh giá thành công");
    }
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @GetMapping("/{productId}")
    public ResponseEntity<List<RatingsResponseDTO>> getRatings(@PathVariable Integer productId) {
        return ResponseEntity.ok(ratingsService.getRatingsByProductId(productId));
    }
}
