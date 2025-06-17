package com.example.myappbackend.controller;

import com.example.myappbackend.dto.request.UpdateProfileRequest;
import com.example.myappbackend.dto.response.UserProfileResponse;
import com.example.myappbackend.exception.BusinessException;
import com.example.myappbackend.model.User;
import com.example.myappbackend.model.UserProfile;
import com.example.myappbackend.repository.UserProfileRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin({"http://localhost:5173", "http://localhost:3000"})
public class UserProfileController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")) {
            throw BusinessException.unauthorized("Bạn chưa đăng nhập", "USER_NOT_AUTHENTICATED");
        }

        // Find user by username
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> BusinessException.unauthorized("Không tìm thấy người dùng", "USER_NOT_FOUND"));

        // Get profile using user ID
        UserProfile profile = userProfileRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> BusinessException.notFound("Không tìm thấy hồ sơ người dùng", "USER_PROFILE_NOT_FOUND"));

        UserProfileResponse dto = new UserProfileResponse(
                profile.getAddress(),
                profile.getAvatarUrl(),
                profile.getBirthDate(),
                profile.getFullName(),
                profile.getProfileId()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw BusinessException.unauthorized("Bạn chưa đăng nhập", "USER_NOT_AUTHENTICATED");
        }

        // Find user by username directly from repository
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> BusinessException.unauthorized("Không tìm thấy người dùng", "USER_NOT_FOUND"));

        // Update user information (email, phone) if provided
        if (request.getEmail() != null || request.getPhone() != null) {
            userService.updateUserInfo(user.getUserId(), request.getEmail(), request.getPhone());
        }

        // Update the profile with the request data
        UserProfile updatedProfile = userService.updateUserProfile(
                user.getUserId(),
                request.getFullName(),
                request.getAddress(),
                request.getBirthDate(),
                request.getAvatarUrl()
        );

        UserProfileResponse dto = new UserProfileResponse(
                updatedProfile.getAddress(),
                updatedProfile.getAvatarUrl(),
                updatedProfile.getBirthDate(),
                updatedProfile.getFullName(),
                updatedProfile.getProfileId()
        );

        return ResponseEntity.ok(dto);
    }
}