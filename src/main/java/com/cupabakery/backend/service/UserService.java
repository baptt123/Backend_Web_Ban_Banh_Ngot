package com.cupabakery.backend.service;

import com.cupabakery.backend.dto.RegisterDTO;
import com.cupabakery.backend.dto.RoleDTO;
import com.cupabakery.backend.dto.UserDTO;
import com.cupabakery.backend.exception.BusinessException;
import com.cupabakery.backend.model.Role;
import com.cupabakery.backend.model.User;
import com.cupabakery.backend.repository.RoleRepository;
import com.cupabakery.backend.repository.UserRepository;
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

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {
        // Check if username is already exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw BusinessException.badRequest(
                    "Username đã được sử dụng: " + registerDTO.getUsername(),
                    "USERNAME_ALREADY_EXISTS"
            );
        }

        // Check if email is already exists
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw BusinessException.badRequest(
                    "Email đã được sử dụng: " + registerDTO.getEmail(),
                    "EMAIL_ALREADY_EXISTS"
            );
        }

        // Encode password using Bcrypt
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // Create new User to save in database
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(encodedPassword);
        // Set default role for client register
        Role customerRole = roleRepository.findByName("CUSTOMER");
        user.setRole(customerRole);

        // Save to database (SQL auto generate by JPA)
        User savedUser = userRepository.save(user);

        // Convert Entity to UserDTO to return for Controller -> Client
        return convertToDTO(savedUser);
    }

    // To create User DTO without sensitive information like (password, ...) == pickUser() Express
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreatedAt(user.getCreatedAt());

        // Convert RoleDTO
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(user.getRole().getId());
        roleDTO.setName(user.getRole().getName());
        userDTO.setRole(roleDTO);

        return userDTO;
    }
}
