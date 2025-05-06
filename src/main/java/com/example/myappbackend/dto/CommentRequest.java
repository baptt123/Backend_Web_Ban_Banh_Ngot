package com.example.myappbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String username;
    private String avatarUrl;
    private String content;
}
