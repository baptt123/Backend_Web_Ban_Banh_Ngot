package com.example.myappbackend.controller;

import com.example.myappbackend.dto.response.UserResponse;
import com.example.myappbackend.service.impl.StoreUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.checkerframework.framework.qual.HasQualifierParameter;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StoreUserController {

    private final StoreUserServiceImpl storeUserService;
    @PreAuthorize("MANAGER")
    @GetMapping("/getusers")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(storeUserService.getUsersByStore(1));
    }
    @PreAuthorize("MANAGER")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        storeUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
