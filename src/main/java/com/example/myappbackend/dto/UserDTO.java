package com.example.myappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*
 * UserDTO - Data object for response to client without sensitive data fields
 * (convert User Entity -> DTO -> return)
*/
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt;
    private RoleDTO role;
}
