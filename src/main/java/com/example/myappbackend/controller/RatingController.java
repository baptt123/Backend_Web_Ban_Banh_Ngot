package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.RatingRequestDTO;
import com.example.myappbackend.dto.RatingResponseDTO;
import com.example.myappbackend.service.interfaceservice.RatingService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody RatingRequestDTO request, HttpServletRequest httpRequest) {
        String token = extractTokenFromCookie(httpRequest);
        ratingService.addRating(request, token);
        return ResponseEntity.ok("Rating added successfully");
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PutMapping
    public ResponseEntity<?> updateRating(@RequestBody RatingRequestDTO request, HttpServletRequest httpRequest) {
        String token = extractTokenFromCookie(httpRequest);
        ratingService.updateRating(request, token);
        return ResponseEntity.ok("Rating updated successfully");
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RatingResponseDTO>> getRatingsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(ratingService.getRatingsByProductId(productId));
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<RatingResponseDTO>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
