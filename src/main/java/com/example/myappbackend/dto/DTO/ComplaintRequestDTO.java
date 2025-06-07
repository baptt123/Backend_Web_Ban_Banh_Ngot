package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data
public class ComplaintRequestDTO {
    private Integer customerId;
    private String complaintText;
    private String adminResponse;
}
