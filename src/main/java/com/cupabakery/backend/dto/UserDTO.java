package com.cupabakery.backend.dto;

import com.cupabakery.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
 * UserDTO - Data object for response to client without sensitive data fields
 * (convert User Entity -> DTO -> return)
*/
public class UserDTO {
    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private RoleDTO role;
}
