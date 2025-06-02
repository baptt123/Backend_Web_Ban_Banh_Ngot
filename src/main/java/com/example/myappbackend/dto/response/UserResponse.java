package com.example.myappbackend.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Integer userId;
    private String fullName;
    private String email;
    private String role;
    private int deleted; // 0 for active, 1 for deleted
}
