package com.example.myappbackend.controller;


import com.example.myappbackend.dto.request.CommentRequest;
import com.example.myappbackend.dto.response.CommentResponse;
import com.example.myappbackend.service.interfaceservice.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addcomment")
    public CommentResponse createComment(@RequestBody CommentRequest request) {
        return commentService.createComment(request);
    }

    @GetMapping("/comments")
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @DeleteMapping("/deletecomment/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
    }
}
