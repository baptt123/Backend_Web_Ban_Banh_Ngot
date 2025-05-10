package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.CommentRequest;
import com.example.myappbackend.dto.CommentResponse;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Comment;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.CommentRepository;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.UserRepository;

import com.example.myappbackend.service.interfaceservice.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CommentResponse addComment(CommentRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Products product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setContent(dto.getContent());

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(
                savedComment.getId(),
                user.getUsername(),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }

    @Override
    public List<CommentResponse> getCommentsByProductId(Integer productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return commentRepository.findByProduct(product)
                .stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getUser().getUsername(),
                        comment.getContent(),
                        comment.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
