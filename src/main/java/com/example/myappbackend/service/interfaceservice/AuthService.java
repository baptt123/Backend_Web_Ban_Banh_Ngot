package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.LoginRequest;
import com.example.myappbackend.dto.request.RegisterRequest;
import com.example.myappbackend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
