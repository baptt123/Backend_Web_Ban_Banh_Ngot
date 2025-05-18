package com.example.myappbackend.service;

import com.example.myappbackend.model.ResetPasswordToken;
import com.example.myappbackend.model.VerificationToken;
import com.example.myappbackend.dto.request.RegisterRequest;
import com.example.myappbackend.dto.RoleDTO;
import com.example.myappbackend.dto.UserDTO;
import com.example.myappbackend.exception.BusinessException;
import com.example.myappbackend.model.Role;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.ResetPasswordTokenRepository;
import com.example.myappbackend.repository.RoleRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Service - Business logic related to User
 * Controller -> Service -> Repository (Model Layer in Express.js)
 * @Transactional make sure if you have error -> throw error to client and stop process
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            VerificationTokenRepository tokenRepository,
            ResetPasswordTokenRepository resetPasswordTokenRepository,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.emailService = emailService;
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

        // Save to database (SQL auto generate by JPA)
        User savedUser = userRepository.save(user);

        // Send verification email
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(savedUser, token);

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
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email: " + email));

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
}