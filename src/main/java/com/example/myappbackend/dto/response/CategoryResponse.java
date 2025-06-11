package com.example.myappbackend.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Integer categoryId;
    private String name;
    private String description;
}
