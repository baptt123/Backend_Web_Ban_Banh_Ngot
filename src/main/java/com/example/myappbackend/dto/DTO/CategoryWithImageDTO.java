package com.example.myappbackend.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CategoryWithImageDTO {
    private Integer categoryId;
    private String name;
    private String imageUrl;
}
