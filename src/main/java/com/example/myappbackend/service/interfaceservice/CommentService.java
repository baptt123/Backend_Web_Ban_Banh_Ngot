package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.CommentRequest;
import com.example.myappbackend.model.Comment;

public interface CommentService {
    Comment createComment(CommentRequest request);
}
