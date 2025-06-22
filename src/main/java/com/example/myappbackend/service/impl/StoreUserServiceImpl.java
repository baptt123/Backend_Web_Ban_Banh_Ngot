//package com.example.myappbackend.service.impl;
//
//import com.example.myappbackend.dto.DTO.UserResponseDTO;
//import com.example.myappbackend.dto.request.UserRoleUpdateRequest;
//import com.example.myappbackend.dto.response.UserResponse;
//import com.example.myappbackend.exception.ResourceNotFoundException;
//import com.example.myappbackend.model.User;
//import com.example.myappbackend.repository.UserRepository;
//import com.example.myappbackend.service.interfaceservice.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class StoreUserServiceImpl implements UserService {
//
//    private final UserRepository usersRepository;
//
//
//    @Override
//    public List<UserResponseDTO> getUsers() {
//        return null;
//    }
//
//    @Override
//    public void updateUserRole(UserRoleUpdateRequest request) {
//
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return List.of();
//    }
//
//    @Override
//    public User getUserById(Integer id) {
//        return null;
//    }
//
//    @Override
//    public User getUserByUsername(String username) {
//        return null;
//    }
//
//    @Override
//    public User updateUser(Integer id, User user) {
//        return null;
//    }
//
//    @Override
//    public void deleteUser(Integer id) {
//
//    }
//
//    @Override
//    public List<UserResponse> getUsersByStore(Integer storeId) {
//        return usersRepository.findByStore_StoreIdAndDeleted(storeId, 0)
//                .stream()
//                .map(user -> {
//                    UserResponse res = new UserResponse();
//                    res.setUserId(user.getUserId());
//                    res.setFullName(user.getUsername());
//                    res.setEmail(user.getEmail());
//                    res.setRole(user.getRole().getName());
//                    return res;
//                })
//                .collect(Collectors.toList());
//    }
//    //hàm này bị dư,đừng để ý
//    public User getCurrentUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return usersRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//}
