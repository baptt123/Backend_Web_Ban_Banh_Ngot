package com.example.myappbackend.controller;

import com.example.myappbackend.dto.RegisterDTO;
import com.example.myappbackend.dto.UserDTO;
import com.example.myappbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        // Controller via @Valid validate all notation in registerDTO
        UserDTO userDTO = userService.registerUser(registerDTO);
        // pickUser() or userDTO and status code 201: CREATED
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }
}