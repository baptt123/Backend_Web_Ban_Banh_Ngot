package com.example.myappbackend.controller;

import com.example.myappbackend.dto.CommentRequest;
import com.example.myappbackend.model.Comment;
import com.example.myappbackend.service.interfaceservice.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Component
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add-comment")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest request) {
        Comment comment = commentService.createComment(request);
        return ResponseEntity.ok(comment);
    }
}

