package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.CommentRequestDTO;
import com.example.myappbackend.dto.DTO.CommentResponseDTO;
import com.example.myappbackend.dto.request.CommentRequest;
import com.example.myappbackend.dto.response.CommentResponse;
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
    public CommentResponse createComment(CommentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setContent(request.getContent());

        Comment saved = commentRepository.save(comment);
        return mapToResponse(saved);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy comment " + id));
        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUserName(comment.getUser().getUsername());
        response.setProductId(comment.getProduct().getProductId());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO dto) {
        Products product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Tạm thời dùng userId = 1 để test
        User user = userRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setProduct(product);
        comment.setUser(user);
        commentRepository.save(comment);

        return mapToResponseDTO(comment);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByProductId(Integer productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return commentRepository.findByProduct(product)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDTO updateComment(Integer commentId, CommentRequestDTO dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        return mapToResponseDTO(comment);
    }

    @Override
    public void deleteCommentManagement(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }

    private CommentResponseDTO mapToResponseDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
