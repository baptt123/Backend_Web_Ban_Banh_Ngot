package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.UserResponseDTO;
import com.example.myappbackend.dto.request.UserRoleUpdateRequest;
import com.example.myappbackend.dto.response.UserResponse;
import com.example.myappbackend.model.Role;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.RoleRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.interfaceservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Override
    public List<UserResponseDTO> getUsers() {
        return userRepository.findByDeleted(0).stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole().getName(),
                        user.isActive()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserRole(UserRoleUpdateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeleted(1);
            userRepository.save(user);

//             userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy user với ID: " + id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User updateUser(Integer id, User user) {
        return null;
    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public List<UserResponse> getUsersByStore(Integer storeId) {
        return null;
    }
}
