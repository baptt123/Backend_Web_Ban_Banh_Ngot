package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.RatingRequest;
import com.example.myappbackend.dto.response.RatingResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public RatingResponse addRating(RatingRequest request) {
        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung"));

        Ratings rating = new Ratings();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(request.getRating());

        Ratings saved = ratingRepository.save(rating);

        return toResponse(saved);
    }

    @Override
    public List<RatingResponse> getAllRatings() {
        return ratingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private RatingResponse toResponse(Ratings r) {
        RatingResponse res = new RatingResponse();
        res.setId(r.getId());
        res.setProductId(r.getProduct().getProductId());
        res.setUserId(r.getUser().getUserId());
        res.setRating(r.getRating());
        res.setCreatedAt(r.getCreatedAt());
        return res;
    }
    @Override
    public RatingResponse updateRating(RatingRequest request) {
        Ratings rating = ratingRepository.findByProductIdAndUserId(
                request.getProductId(),
                request.getUserId()
        ).orElseThrow(() -> new RuntimeException("Rating not found"));

        rating.setRating(request.getRating());  // Cập nhật số sao

        Ratings updated = ratingRepository.save(rating);

        return toResponse(updated);
    }

}
