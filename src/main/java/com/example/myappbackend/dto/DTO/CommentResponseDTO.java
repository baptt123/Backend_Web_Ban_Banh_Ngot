package com.example.myappbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String fullName;
    private String avatarUrl;
}
