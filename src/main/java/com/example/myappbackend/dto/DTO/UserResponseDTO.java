package com.example.myappbackend.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private String roleName;
    private boolean active;
}
