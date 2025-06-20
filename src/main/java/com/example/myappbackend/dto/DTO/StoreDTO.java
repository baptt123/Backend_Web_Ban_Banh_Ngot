package com.example.myappbackend.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;

    // Chỉ cần thông tin cơ bản của manager, tránh đệ quy
    private Integer managerId;
    private String managerUsername;
}
