package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.AuthResponse;
import com.example.myappbackend.dto.LoginRequest;
import com.example.myappbackend.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
