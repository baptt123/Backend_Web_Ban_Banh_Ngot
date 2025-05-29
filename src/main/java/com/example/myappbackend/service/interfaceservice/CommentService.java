package com.example.myappbackend.service.interfaceservice;



import com.example.myappbackend.dto.request.CommentRequest;
import com.example.myappbackend.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest request);
    List<CommentResponse> getAllComments();
    void deleteComment(Integer id);
}
