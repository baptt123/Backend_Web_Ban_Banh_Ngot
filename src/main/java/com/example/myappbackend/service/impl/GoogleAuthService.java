package com.example.myappbackend.service.impl;

import com.example.myappbackend.exception.BusinessException;
import com.example.myappbackend.model.Role;
import com.example.myappbackend.model.User;
import com.example.myappbackend.model.UserProfile;
import com.example.myappbackend.repository.RoleRepository;
import com.example.myappbackend.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

    @Value("${google.client.id}")
    private String googleClientId;

    public GoogleAuthService(UserRepository userRepository,
                             UserService userService,
                             PasswordEncoder passwordEncoder,
                             RoleRepository roleRepository,
                             JwtService jwtService,
                             UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.userProfileRepository = userProfileRepository;
    }

    public User processGoogleLogin(String googleToken) throws Exception {
        try {
            // Exchange Google token for ID token using Google's client library'
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(googleToken);

            // Check if token is valid
            if (idToken == null) {
                System.out.println("Token Google không hợp lệ");
                throw BusinessException.badRequest("Token Google không hợp lệ", "INVALID_GOOGLE_TOKEN");
            }

            // Get user's info from ID token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();

            // Create a username from email
            String username = email.substring(0, email.indexOf('@'));

            // Check if a user exists in the database
            Optional<User> userByEmail = userRepository.findByEmail(email);

            User user;
            // Get the existing account
            if (userByEmail.isPresent()) {
                user = userByEmail.get();
                // Adding google info if an account exists
                if (user.getGoogleId() == null || !user.getGoogleId().equals(googleId)) {
                    // Update existing user with Google ID
                    user.setGoogleId(googleId);
                    user = userRepository.save(user);

                    // Update the profile with Google information if it exists
                    String fullName = (String) payload.get("name");
                    String avatarUrl = (String) payload.get("picture");

                    UserProfile profile = userProfileRepository.findByUser(user)
                            .orElseThrow(() -> BusinessException.badRequest("Không tìm thấy hồ sơ người dùng", "USER_PROFILE_NOT_FOUND"));
                    profile.setFullName(fullName);
                    profile.setAvatarUrl(avatarUrl);
                    userProfileRepository.save(profile);
                }
            } else user = createNewGoogleUser(username, email, googleId, payload);

            return user;
        } catch (Exception e) {
            System.out.println("Lỗi khi xác thực Google token: {}" + e.getMessage() + e);
            throw e;
        }
    }

    private User createNewGoogleUser(String username, String email, String googleId, GoogleIdToken.Payload payload) {
        // Make sure username is unique
        if (userRepository.existsByUsername(username)) {
            username = username + new Random().nextInt(1000);
        }

        // Create random password for new user
        String randomPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(randomPassword);

        // Set default role for client register
        Role customerRole = roleRepository.findByName("CUSTOMER");

        // Get Google profile info from the payload
        String fullName = (String) payload.get("name");
        String avatarUrl = (String) payload.get("picture");

        // Create a new User to save in a database
        User user = User.builder()
                .username(username)
                .email(email)
                .googleId(googleId)
                .password(encodedPassword)
                .role(customerRole)
                .active(true)
                .build();

        // Save the user first to get the user ID
        User savedUser = userRepository.save(user);

        // Create a user profile with Google information
        UserProfile userProfile = UserProfile.builder()
                .user(savedUser)
                .fullName(fullName)
                .avatarUrl(avatarUrl)
                .build();
        userProfileRepository.save(userProfile);

        return savedUser;
    }
}
