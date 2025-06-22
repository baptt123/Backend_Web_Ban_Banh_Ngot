package com.example.myappbackend.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Integer categoryId;
    private String name;
    private String description;
    private String storeName;
}