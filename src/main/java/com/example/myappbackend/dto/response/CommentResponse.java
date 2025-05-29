package com.example.myappbackend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Integer id;
    private String content;
    private String userName;
    private Integer productId;
    private LocalDateTime createdAt;
}
