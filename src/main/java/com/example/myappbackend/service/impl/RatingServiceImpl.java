package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.RatingRequestDTO;
import com.example.myappbackend.dto.RatingResponseDTO;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Ratings;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.RatingRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.interfaceservice.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtService jwtUtils;

    @Override
    public void addRating(RatingRequestDTO request, String token) {
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if already rated
        if (ratingRepository.findByProductAndUser(product, user).isPresent()) {
            throw new RuntimeException("You have already rated this product");
        }

        Ratings rating = new Ratings();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRating(request.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);
    }

    @Override
    public void updateRating(RatingRequestDTO request, String token) {
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Ratings rating = ratingRepository.findByProductAndUser(product, user)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        rating.setRating(request.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);
    }

    @Override
    public List<RatingResponseDTO> getRatingsByProductId(Integer productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return ratingRepository.findByProduct(product)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponseDTO> getAllRatings() {
        return ratingRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RatingResponseDTO convertToDto(Ratings rating) {
        RatingResponseDTO dto = new RatingResponseDTO();
        dto.setId(rating.getId());
        dto.setRating(rating.getRating());
        dto.setCreatedAt(rating.getCreatedAt());

        User user = rating.getUser();
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getUserProfile() != null ? user.getUserProfile().getFullName() : null);
        dto.setAvatarUrl(user.getUserProfile() != null ? user.getUserProfile().getAvatarUrl() : null);
        return dto;
    }
}
