package com.example.myappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@AllArgsConstructor
@Data
public class CommentResponse {
    private Integer id;
    private String username;
    private String content;
    private LocalDateTime createdAt;

}
