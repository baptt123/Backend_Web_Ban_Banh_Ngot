package com.example.myappbackend.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Integer userId;
    private Integer productId;
    private String content;
}
