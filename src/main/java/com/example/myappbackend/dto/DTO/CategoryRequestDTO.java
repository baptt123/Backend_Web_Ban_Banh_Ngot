package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;
    private String description;
    private Integer storeId;
}
