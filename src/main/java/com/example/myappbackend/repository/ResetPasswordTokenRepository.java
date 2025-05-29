package com.example.myappbackend.repository;

import com.example.myappbackend.model.ResetPasswordToken;
import com.example.myappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    Optional<ResetPasswordToken> findByToken(String token);
    void deleteByUser(User user);
}

