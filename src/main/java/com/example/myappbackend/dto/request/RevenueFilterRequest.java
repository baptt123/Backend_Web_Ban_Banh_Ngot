package com.example.myappbackend.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RevenueFilterRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
