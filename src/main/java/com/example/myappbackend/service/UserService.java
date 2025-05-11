package com.example.myappbackend.service;

import com.example.myappbackend.dto.RegisterDTO;
import com.example.myappbackend.dto.UserDTO;
import com.example.myappbackend.exception.EmailExistsException;
import com.example.myappbackend.exception.UsernameExistsException;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {
        // Check if username is already exists
        if (userRepository.existsByUserName(registerDTO.getUsername())) {
            throw new UsernameExistsException("Username đã được sử dụng");
        }

        // Check if email is already exists ?
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new EmailExistsException("Email đã được sử dụng");
        }

        // Hash password using Argon2
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // Create new User to save in database
        User user = new User();
        user.setUserName(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encodedPassword);

        // Save to database (SQL auto generate by JPA)
        User savedUser = userRepository.save(user);

        // Convert to UserDTO to return for Controller -> Client
        return convertToDTO(savedUser);
    }

    // To create User DTO without sensitive information like (password, ...) == pickUser() Express
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }
}
