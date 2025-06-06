package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Integer productId;
    private String content;
}
