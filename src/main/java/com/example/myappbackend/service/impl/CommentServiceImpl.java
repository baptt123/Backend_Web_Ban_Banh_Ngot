package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.CommentRequest;
import com.example.myappbackend.model.Comment;
import com.example.myappbackend.repository.CommentRepository;
import com.example.myappbackend.service.interfaceservice.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment createComment(CommentRequest request) {
        Comment comment = new Comment();
        comment.setUsername(request.getUsername());
        comment.setAvatarUrl(request.getAvatarUrl());
        comment.setContent(request.getContent());

        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        comment.setCreatedAt(currentTime);

        return commentRepository.save(comment);
    }
}
