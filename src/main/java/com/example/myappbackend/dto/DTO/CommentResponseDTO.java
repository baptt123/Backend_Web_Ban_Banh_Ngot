package com.example.myappbackend.dto.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDTO {
    private Integer id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
}
