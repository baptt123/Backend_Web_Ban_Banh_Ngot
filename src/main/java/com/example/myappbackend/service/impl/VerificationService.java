package com.example.myappbackend.service.impl;

import com.example.myappbackend.model.User;
import com.example.myappbackend.model.VerificationToken;
import com.example.myappbackend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    public boolean verifyToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setActive(true);
        tokenRepository.delete(verificationToken);
        return true;
    }
}

