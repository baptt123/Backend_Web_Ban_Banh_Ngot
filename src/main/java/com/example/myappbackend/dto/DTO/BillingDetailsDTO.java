package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data // Annotation của Lombok để tự tạo getter, setter, toString...
public class BillingDetailsDTO {
    private String fname;
    private String lname;
    private String country;
    private String address;
    private String email;
    private String phone;
    // Thêm các trường khác nếu frontend có gửi
}