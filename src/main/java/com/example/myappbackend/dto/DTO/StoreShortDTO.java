package com.example.myappbackend.dto.DTO;

import lombok.Data;

@Data
public class StoreShortDTO {
    private Integer storeId;
    private String storeName;
    private Integer stock;
}