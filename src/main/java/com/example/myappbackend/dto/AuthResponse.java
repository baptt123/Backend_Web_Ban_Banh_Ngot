package com.example.myappbackend.dto;

import com.example.myappbackend.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Integer userId;
    private String username;
    private String email;
    private Roles role;
    private String token;
    private String message;
}
