package com.example.myappbackend.service.impl;

import com.example.myappbackend.model.*;
import com.example.myappbackend.dto.request.RegisterRequest;
import com.example.myappbackend.dto.DTO.RoleDTO;
import com.example.myappbackend.dto.DTO.UserDTO;
import com.example.myappbackend.exception.BusinessException;
import com.example.myappbackend.repository.ResetPasswordTokenRepository;
import com.example.myappbackend.repository.RoleRepository;
import com.example.myappbackend.repository.UserProfileRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/*
 * Service - Business logic related to User
 * Controller -> Service -> Repository (Model Layer in Express.js)
 * @Transactional make sure if you have error -> throw error to the client and stop the process
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final EmailService emailService;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            VerificationTokenRepository tokenRepository,
            ResetPasswordTokenRepository resetPasswordTokenRepository,
            EmailService emailService,
            UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.emailService = emailService;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public UserDTO registerUser(RegisterRequest registerRequest) {
        // Check if username is already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw BusinessException.badRequest(
                    "Username đã được sử dụng: " + registerRequest.getUsername(),
                    "USERNAME_ALREADY_EXISTS"
            );
        }

        // Check if email is already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw BusinessException.badRequest(
                    "Email đã được sử dụng: " + registerRequest.getEmail(),
                    "EMAIL_ALREADY_EXISTS"
            );
        }

        // Encode password using Bcrypt
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Set default role for client register
        Role customerRole = roleRepository.findByName("CUSTOMER");

        // Create new User to save in database
        // Default: ROLE_customer, active: false
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .password(encodedPassword)
                .role(customerRole)
                .active(false)
                .build();

        User savedUser = userRepository.save(user);

        // Create default user profile with username as fullName
        UserProfile userProfile = UserProfile.builder()
                .user(savedUser)
                .fullName(registerRequest.getUsername()) // Default to username
                .build();
        userProfileRepository.save(userProfile);

        // Send verification email
        sendVerificationEmailToUser(savedUser);

        // Convert Entity to UserDTO to return for Controller -> Client
        return convertToDTO(savedUser);
    }

    // To create User DTO without sensitive information like (password, ...) == pickUser() Express
    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .active(user.isActive())
                .role(
                        RoleDTO.builder()
                                .id(user.getRole().getId())
                                .name(user.getRole().getName())
                                .build()
                )
                .build();
    }

    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> BusinessException.notFound("Không tồn tại tài khoản: " + email, "USER_NOT_FOUND"));

        // Xóa token cũ nếu có
        resetPasswordTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetToken = ResetPasswordToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        resetPasswordTokenRepository.save(resetToken);
        emailService.sendResetPasswordEmail(user, token);
    }
  
    // Update user profile information
    @Transactional
    public UserProfile updateUserProfile(Integer userId, String fullName,
                                        String address, LocalDate birthDate, 
                                        String avatarUrl) {
        // Get profile directly from repository
        UserProfile profile = userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> BusinessException.notFound(
                    "Profile not found for user: " + userId, 
                    "PROFILE_NOT_FOUND"
                ));

        // Only update fields that are provided (not null)
        if (fullName != null) {
            profile.setFullName(fullName);
        }
        if (address != null) {
            profile.setAddress(address);
        }
        if (birthDate != null) {
            profile.setBirthDate(birthDate);
        }
        if (avatarUrl != null) {
            profile.setAvatarUrl(avatarUrl);
        }

        return userProfileRepository.save(profile);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> BusinessException.badRequest(
                        "Token không hợp lệ hoặc đã hết hạn",
                        "INVALID_TOKEN"
                ));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw BusinessException.badRequest("Token hết hạn", "TOKEN_EXPIRED");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetPasswordTokenRepository.delete(resetToken);
    }

    // Update user email and phone (from update profile action)
    @Transactional
    public User updateUserInfo(Integer userId, String email, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound(
                    "User not found: " + userId, 
                    "USER_NOT_FOUND"
                ));

        boolean emailChanged = false;
        
        // Update email if provided and different from current
        if (email != null && !email.equals(user.getEmail())) {
            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                throw BusinessException.badRequest(
                    "Email đã được sử dụng", 
                    "EMAIL_ALREADY_EXISTS"
                );
            }
            user.setEmail(email);
            user.setActive(false);
            emailChanged = true;
        }
        
        // Update phone if provided
        if (phone != null) {
            user.setPhone(phone);
        }

        User savedUser = userRepository.save(user);
        
        // Send verification email if email was changed
        if (emailChanged) {
            sendVerificationEmailToUser(savedUser);
        }
        
        return savedUser;
    }

    @Transactional
    public void sendVerificationEmailToUser(User user) {
        // Generate verification token
        String token = UUID.randomUUID().toString();
        
        // Add new token to database
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24)) // 24 hours expiry
                .build();
        tokenRepository.save(verificationToken);
        
        // Send email
        emailService.sendVerificationEmail(user, token);
    }
}