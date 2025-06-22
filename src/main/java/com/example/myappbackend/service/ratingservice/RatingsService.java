package com.example.myappbackend.service.ratingservice;

import com.example.myappbackend.dto.ratingDTO.RatingsRequestDTO;
import com.example.myappbackend.dto.ratingDTO.RatingsResponseDTO;
import com.example.myappbackend.exception.UnauthorizedRatingException;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Ratings;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.RatingRepository;
import com.example.myappbackend.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.example.myappbackend")
public class RatingsService {
    private final RatingRepository ratingsRepository;
    private final ProductRepository productsRepository;


    public void addRating(RatingsRequestDTO dto, User userToken) {
        User user = userToken;
        if (user == null) throw new UnauthorizedRatingException("Bạn phải đăng nhập để đánh giá");

        Products product = productsRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Ratings rating = new Ratings();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRating(dto.getRating());
        ratingsRepository.save(rating);
    }

    public List<RatingsResponseDTO> getRatingsByProductId(Integer productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        return ratingsRepository.findByProduct(product).stream()
                .map(r -> RatingsResponseDTO.builder()
                        .fullName(r.getUser().getUserProfile().getFullName())
                        .avatarUrl(r.getUser().getUserProfile().getAvatarUrl())
                        .rating(r.getRating())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
