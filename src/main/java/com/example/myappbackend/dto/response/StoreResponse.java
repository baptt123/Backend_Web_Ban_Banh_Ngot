package com.example.myappbackend.dto.response;

import lombok.Data;

@Data
public class StoreResponse {
    private Integer storeId;
    private String name;
    private String address;
    private String phone;
}
