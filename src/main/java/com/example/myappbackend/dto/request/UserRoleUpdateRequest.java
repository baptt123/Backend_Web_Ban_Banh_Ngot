package com.example.myappbackend.dto.request;

import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    private Integer userId;
    private Integer roleId;
}