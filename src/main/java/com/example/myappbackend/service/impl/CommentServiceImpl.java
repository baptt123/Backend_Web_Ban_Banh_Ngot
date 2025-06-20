package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.CommentResponseDTO;
import com.example.myappbackend.dto.DTO.CommentRequestDTO;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.*;
import com.example.myappbackend.repository.CommentRepository;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.interfaceservice.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtService jwtUtils;

    @Override
    public void addComment(CommentRequestDTO request, String token) {
        String username = jwtUtils.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setContent(request.getContent());
        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByProductId(Integer productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return commentRepository.findByProductAndDeletedFalseOrderByCreatedAtDesc(product)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDTO> getAllComments() {
        return commentRepository.findByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO convertToDto(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        User user = comment.getUser();
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getUserProfile() != null ? user.getUserProfile().getFullName() : null);
        dto.setAvatarUrl(user.getUserProfile() != null ? user.getUserProfile().getAvatarUrl() : null);
        return dto;
    }
}
