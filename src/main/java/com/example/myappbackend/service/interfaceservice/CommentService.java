package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.CommentResponseDTO;
import com.example.myappbackend.dto.DTO.CommentRequestDTO;

import java.util.List;

public interface CommentService {
    void addComment(CommentRequestDTO commentRequest, String token);
    List<CommentResponseDTO> getCommentsByProductId(Integer productId);
    List<CommentResponseDTO> getAllComments();
}
