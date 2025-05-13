package com.cupabakery.backend.service;

import com.cupabakery.backend.model.User;
import com.cupabakery.backend.model.VerificationToken;
import com.cupabakery.backend.repository.VerificationTokenRepository;
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

