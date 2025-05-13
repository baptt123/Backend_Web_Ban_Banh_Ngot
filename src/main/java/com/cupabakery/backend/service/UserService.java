package com.cupabakery.backend.service;

import com.cupabakery.backend.model.VerificationToken;
import com.cupabakery.backend.model.request.RegisterRequest;
import com.cupabakery.backend.dto.RoleDTO;
import com.cupabakery.backend.dto.UserDTO;
import com.cupabakery.backend.exception.BusinessException;
import com.cupabakery.backend.model.Role;
import com.cupabakery.backend.model.User;
import com.cupabakery.backend.repository.RoleRepository;
import com.cupabakery.backend.repository.UserRepository;
import com.cupabakery.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

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
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .role(
                        RoleDTO.builder()
                                .id(user.getRole().getId())
                                .name(user.getRole().getName())
                                .build()
                )
                .build();
    }
}
