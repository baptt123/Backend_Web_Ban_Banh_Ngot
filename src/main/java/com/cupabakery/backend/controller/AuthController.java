package com.cupabakery.backend.controller;

import com.cupabakery.backend.model.request.RegisterRequest;
import com.cupabakery.backend.dto.UserDTO;
import com.cupabakery.backend.service.UserService;
import com.cupabakery.backend.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final VerificationService verificationService;

    @Autowired
    public AuthController(UserService userService, VerificationService verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = userService.registerUser(registerRequest);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean verified = verificationService.verifyToken(token);
        if (verified) {
            return ResponseEntity.ok("Xác thực email thành công!");
        }
        return ResponseEntity.badRequest().body("Token không hợp lệ hoặc hết hạn.");
    }
}