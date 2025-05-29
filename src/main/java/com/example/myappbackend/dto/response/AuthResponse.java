package com.example.myappbackend.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String scope;
    private String access_token;
    private String token_type;
    private String app_id;
    private int expires_in;
    private String nonce;
}