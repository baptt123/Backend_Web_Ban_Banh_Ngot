package com.example.myappbackend.dto.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintResponseDTO {
    private Integer id;
    private String customerUsername;
    private String complaintText;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
