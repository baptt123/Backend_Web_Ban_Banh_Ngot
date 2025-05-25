package com.example.myappbackend.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Integer userId;
    private Integer productId;
    private String content;
}
