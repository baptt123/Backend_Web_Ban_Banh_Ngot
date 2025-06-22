package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.UserResponseDTO;
import com.example.myappbackend.dto.request.UserRoleUpdateRequest;
import com.example.myappbackend.dto.response.UserResponse;
import com.example.myappbackend.model.User;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getUsers();
    void updateUserRole(UserRoleUpdateRequest request);
    void deleteUserById(Integer id);
    List<User> getAllUsers();
    User getUserById(Integer id);
    User getUserByUsername(String username);
    User updateUser(Integer id, User user);
    void deleteUser(Integer id);
    List<UserResponse> getUsersByStore(Integer storeId);
}
