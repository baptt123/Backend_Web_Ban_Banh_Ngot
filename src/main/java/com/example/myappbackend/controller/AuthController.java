package com.example.myappbackend.controller;


import com.example.myappbackend.dto.request.GoogleLoginRequest;
import com.example.myappbackend.dto.request.RegisterRequest;
import com.example.myappbackend.dto.DTO.UserDTO;
import com.example.myappbackend.dto.request.LoginRequest;
import com.example.myappbackend.dto.request.ResetPasswordRequest;
import com.example.myappbackend.exception.BusinessException;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final GoogleAuthService googleAuthService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.expiration:3600}")
    private long jwtExpiration;

    @Autowired
    public AuthController(UserService userService,
                          UserRepository userRepository,
                          VerificationService verificationService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          GoogleAuthService googleAuthService,
                          CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.verificationService = verificationService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.googleAuthService = googleAuthService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = userService.registerUser(registerRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam(name = "token") String token) {
        boolean verified = verificationService.verifyToken(token);
        if (verified) {
            return ResponseEntity.ok("Xác thực email thành công!");
        }
        return ResponseEntity.badRequest().body("Token không hợp lệ hoặc hết hạn.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> BusinessException.badRequest("Không tìm thấy người dùng", "USER_NOT_FOUND"));

            if (!user.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "message", "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực tài khoản.",
                                "errorCode", "ACCOUNT_NOT_ACTIVATED"
                        ));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

            UserDTO userDTO = userService.convertToDTO(user);

            ResponseCookie jwtCookie = ResponseCookie.from("access_token", token)
                    .httpOnly(true)
//                    .secure(true)
                    .path("/")
                    .maxAge(jwtExpiration)
//                    .sameSite("none")
                    .sameSite("Lax")
                    .build();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Đăng nhập thành công");
            responseBody.put("user", userDTO);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(responseBody);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Tên đăng nhập hoặc mật khẩu không đúng",
                            "errorCode", "INVALID_CREDENTIALS"
                    ));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message", "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực tài khoản.",
                            "errorCode", "ACCOUNT_NOT_ACTIVATED"
                    ));
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        try {
            User user = googleAuthService.processGoogleLogin(request.getToken());

            if (!user.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "message", "Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực tài khoản.",
                                "errorCode", "ACCOUNT_NOT_ACTIVATED"
                        ));
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtService.generateToken(userDetails);

            ResponseCookie jwtCookie = ResponseCookie.from("access_token", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(jwtExpiration)
                    .sameSite("none")
                    .build();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Đăng nhập thành công");
            responseBody.put("user", userService.convertToDTO(user));

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Đăng nhập Google thất bại: " + e.getMessage(),
                            "errorCode", "GOOGLE_LOGIN_FAILED"
                    ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Clear cookie via set maxAge = 0
        ResponseCookie jwtCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("none")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of("message", "Đăng xuất thành công"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", "Email không được để trống",
                            "errorCode", "EMAIL_REQUIRED"
                    ));
        }
        userService.createPasswordResetToken(email);
        return ResponseEntity.ok(Map.of("message", "Email đặt lại mật khẩu đã được gửi."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message","Mật khẩu đã được thay đổi thành công."));
    }
//    //tự động đăng nhập ở frontend bằng jwt
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
//        }
//
//        User user = (User) authentication.getPrincipal(); // hoặc lấy từ SecurityContextHolder
//        UserDTO userDTO = userService.convertToDTO(user);
//
//        return ResponseEntity.ok(userDTO);
//    }

    /**
     * Lấy thông tin người dùng hiện tại từ Authentication
     *
     * @param authentication Authentication chứa thông tin người dùng đã đăng nhập
     * @return Thông tin người dùng dưới dạng UserDTO hoặc lỗi nếu không tìm thấy người dùng
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(userService.convertToDTO(user));
    }
}