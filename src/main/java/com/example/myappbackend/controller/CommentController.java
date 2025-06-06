package com.example.myappbackend.controller;


import com.example.myappbackend.dto.DTO.CommentRequestDTO;
import com.example.myappbackend.dto.DTO.CommentResponseDTO;
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
    @PostMapping
    public CommentResponseDTO createComment(@RequestBody CommentRequestDTO dto) {
        return commentService.createComment(dto);
    }

    @GetMapping("/product/{productId}")
    public List<CommentResponseDTO> getCommentsByProduct(@PathVariable Integer productId) {
        return commentService.getCommentsByProductId(productId);
    }

    @PutMapping("/{commentId}")
    public CommentResponseDTO updateComment(@PathVariable Integer commentId,
                                            @RequestBody CommentRequestDTO dto) {
        return commentService.updateComment(commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentManagement(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }
}
