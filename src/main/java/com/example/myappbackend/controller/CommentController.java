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
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CommentController {

    @Autowired
    private CommentService commentService;

    /*
    This endpoint is used to create a comment for a product by a user.
     */
    @PostMapping("/addcomment")
    public CommentResponse createComment(@RequestBody CommentRequest request) {
        return commentService.createComment(request);
    }

    /*
    This endpoint is used to get all comments for user
     */
    @GetMapping("/comments")
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    /*
    This endpoint is used to delete a comment by its ID(user).
     */
    @DeleteMapping("/deletecomment/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
    }

    /*
    This endpoint is used to create a comment (management by store).
     */
    @PostMapping
    public CommentResponseDTO createComment(@RequestBody CommentRequestDTO dto) {
        return commentService.createComment(dto);
    }

    /*
        This endpoint is used to get all comments for a specific product.
        It takes the product ID as a path variable and returns a list of comments(management by store).
     */
//    @GetMapping("/product/{productId}")
//    public List<CommentResponseDTO> getCommentsByProduct(@PathVariable Integer productId) {
//        return commentService.getCommentsByProductId(productId);
//    }
    @GetMapping("/comments-store")
    public List<CommentResponse> getAllCommentsStore() {
        return commentService.getAllComments();
    }

    /*
        This endpoint is used to update a comment by its ID (management by store).
        It takes the comment ID as a path variable and the updated comment data in the request body.
        It returns the updated comment.(management by store).
     */
    @PutMapping("/{commentId}")
    public CommentResponseDTO updateComment(@PathVariable Integer commentId,
                                            @RequestBody CommentRequestDTO dto) {
        return commentService.updateComment(commentId, dto);
    }

    /*
        This endpoint is used to delete a comment by its ID (management by store).
     */
    @DeleteMapping("/{commentId}")
    public void deleteCommentManagement(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
    }
}
