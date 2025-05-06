//package com.example.myappbackend.service.impl;
//
//import com.example.myappbackend.dto.AuthResponse;
//import com.example.myappbackend.dto.LoginRequest;
//import com.example.myappbackend.dto.RegisterRequest;
//import com.example.myappbackend.model.Role;
//import com.example.myappbackend.model.User;
//import com.example.myappbackend.repository.UserRepository;

//import com.example.myappbackend.service.JwtService;
//import jakarta.persistence.EntityExistsException;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//
//    @Override
//    public AuthResponse register(RegisterRequest request) {
//        // Kiểm tra username đã tồn tại chưa
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new EntityExistsException("Username đã tồn tại");
//        }
//
//        // Kiểm tra email đã tồn tại chưa
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new EntityExistsException("Email đã tồn tại");
//        }
//
//        // Tạo user mới
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setEmail(request.getEmail());
//        user.setPhone(request.getPhone());
//        user.setRole(Role.USER);
//
//        // Lưu user vào database
//        User savedUser = userRepository.save(user);
//
//        // Tạo token JWT
//        String token = jwtService.generateToken(savedUser);
//
//        // Trả về response
//        return AuthResponse.builder()
//                .userId(savedUser.getUserId())
//                .username(savedUser.getUsername())
//                .email(savedUser.getEmail())
//                .role(savedUser.getRole())
//                .token(token)
//                .message("Đăng ký thành công")
//                .build();
//    }
//
//    @Override
//    public AuthResponse login(LoginRequest request) {
//        // Tìm user theo username
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new EntityNotFoundException("Tài khoản không tồn tại"));
//
//        // Kiểm tra password
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Mật khẩu không chính xác");
//        }
//
//        // Tạo token JWT
//        String token = jwtService.generateToken(user);
//
//        // Trả về response
//        return AuthResponse.builder()
//                .userId(user.getUserId())
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .role(user.getRole())
//                .token(token)
//                .message("Đăng nhập thành công")
//                .build();
//    }
//}
