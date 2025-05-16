package com.cupabakery.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO {
    private int id;
    private String name;
}
