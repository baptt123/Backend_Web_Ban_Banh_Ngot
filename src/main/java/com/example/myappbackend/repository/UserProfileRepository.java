package com.example.myappbackend.repository;

import com.example.myappbackend.model.UserProfile;
import com.example.myappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUser_UserId(Integer userId);
}
