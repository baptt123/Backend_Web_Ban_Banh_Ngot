package com.example.myappbackend.service;

import com.example.myappbackend.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer id);
    User getUserByUsername(String username);
    User updateUser(Integer id, User user);
    void deleteUser(Integer id);
}
