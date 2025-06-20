package com.example.myappbackend.controller;

import com.example.myappbackend.dto.CommentResponseDTO;
import com.example.myappbackend.dto.DTO.CommentRequestDTO;
import com.example.myappbackend.service.interfaceservice.CommentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentRequestDTO request, HttpServletRequest httpRequest) {
        String token = extractTokenFromCookie(httpRequest);
        commentService.addComment(request, token);
        return ResponseEntity.ok("Comment added successfully");
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(commentService.getCommentsByProductId(productId));
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/all-comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("access_token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
