package com.example.myappbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object for profile update requests
 */
@Data
public class UpdateProfileRequest {
    private String fullName;

    private String address;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String avatarUrl;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number should be 10-11 digits")
    private String phone;
}