package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.UserResponseDTO;
import com.example.myappbackend.dto.request.UserRoleUpdateRequest;
import com.example.myappbackend.service.interfaceservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody UserRoleUpdateRequest request) {
        userService.updateUserRole(request);
        return ResponseEntity.ok("Cập nhật quyền người dùng thành công!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("Xoá thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
