//package com.example.myappbackend.service.impl;
//
//import com.example.myappbackend.dto.DTO.RatingRequestDTO;
//import com.example.myappbackend.dto.RatingResponseDTO;
//import com.example.myappbackend.dto.ratingDTO.RatingsRequestDTO;
//import com.example.myappbackend.exception.UnauthorizedRatingException;
//import com.example.myappbackend.model.Products;
//import com.example.myappbackend.model.Ratings;
//import com.example.myappbackend.model.User;
//import com.example.myappbackend.repository.ProductRepository;
//import com.example.myappbackend.repository.RatingRepository;
//import com.example.myappbackend.service.interfaceservice.RatingService;
//import org.springframework.stereotype.Service;
//import lombok.RequiredArgsConstructor;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RatingServiceImpl implements RatingService {
//
//    private final RatingRepository ratingsRepository;
//    private final ProductRepository productsRepository;
//
//    @Override
//    public void addRating(RatingRequestDTO request, String token) {
//        if (token == null) throw new UnauthorizedRatingException("Bạn phải đăng nhập để đánh giá");
//
//        Products product = productsRepository.findById(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
//
//        Ratings rating = new Ratings();
//        rating.setUser(user);
//        rating.setProduct(product);
//        rating.setRating(request.getRating());
//        ratingsRepository.save(rating);
//    }
//
//    @Override
//    public void updateRating(RatingRequestDTO request, String token) {
//
//    }
//
//    @Override
//    public List<RatingResponseDTO> getRatingsByProductId(Integer productId) {
//        return null;
//    }
//
//    @Override
//    public List<RatingResponseDTO> getAllRatings() {
//        return null;
//    }
//}
