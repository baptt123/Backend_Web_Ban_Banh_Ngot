package com.cupabakery.backend.controller;

import com.cupabakery.backend.dto.RegisterDTO;
import com.cupabakery.backend.dto.UserDTO;
import com.cupabakery.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        UserDTO userDTO = userService.registerUser(registerDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }
}