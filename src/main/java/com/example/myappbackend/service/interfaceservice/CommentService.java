package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.CommentRequest;
import com.example.myappbackend.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(CommentRequest dto);
    List<CommentResponse> getCommentsByProductId(Integer productId);
}
