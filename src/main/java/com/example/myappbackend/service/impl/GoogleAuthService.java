package com.example.myappbackend.service.impl;

import com.example.myappbackend.exception.BusinessException;
import com.example.myappbackend.model.Role;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.RoleRepository;
import com.example.myappbackend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class GoogleAuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Value("${google.client.id}")
    private String googleClientId;

    public GoogleAuthService(UserRepository userRepository,
                             UserService userService,
                             PasswordEncoder passwordEncoder,
                             RoleRepository roleRepository,
                             JwtService jwtService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public User processGoogleLogin(String googleToken) throws Exception {
        System.out.println("Bắt đầu xác thực Google token với clientId: {}" + googleClientId);

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken == null) {
                System.out.println("Token Google không hợp lệ");
                throw BusinessException.badRequest("Token Google không hợp lệ", "INVALID_GOOGLE_TOKEN");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();

            String username = email.substring(0, email.indexOf('@'));

            Optional<User> userByEmail = userRepository.findByEmail(email);
            Optional<User> userByGoogleId = userRepository.findByGoogleId(googleId);

            User user;

            if (userByEmail.isPresent()) {
                user = userByEmail.get();
                if (user.getGoogleId() == null) {
                    user.setGoogleId(googleId);
                    user = userRepository.save(user);
                }
            } else if (userByGoogleId.isPresent()) {
                user = userByGoogleId.get();
            } else {
                user = createNewGoogleUser(username, email, googleId);
            }

            return user;
        } catch (Exception e) {
            System.out.println("Lỗi khi xác thực Google token: {}" + e.getMessage() + e);
            throw e;
        }
    }

    private User createNewGoogleUser(String username, String email, String googleId) {
        if (userRepository.existsByUsername(username)) {
            username = username + new Random().nextInt(1000);
        }

        String randomPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(randomPassword);

        Role customerRole = roleRepository.findByName("CUSTOMER");

        User user = User.builder()
                .username(username)
                .email(email)
                .googleId(googleId)
                .password(encodedPassword)
                .role(customerRole)
                .active(true)
                .build();

        return userRepository.save(user);
    }
}
