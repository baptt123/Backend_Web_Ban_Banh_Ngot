package com.example.myappbackend.controller;

import com.example.myappbackend.dto.CommentRequest;
import com.example.myappbackend.dto.CommentResponse;
import com.example.myappbackend.service.interfaceservice.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addcomment")
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest dto) {
        CommentResponse response = commentService.addComment(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByProduct(@PathVariable Integer productId) {
        List<CommentResponse> comments = commentService.getCommentsByProductId(productId);
        return ResponseEntity.ok(comments);
    }
}
