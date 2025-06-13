package com.example.myappbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserProfileResponse {
    private String address;
    private String avatarUrl;
    private LocalDate birthDate;
    private String fullName;
    private Integer profileId;
}
