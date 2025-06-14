package com.example.myappbackend.dto.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private String title;
    private BigDecimal price; // Sử dụng BigDecimal cho tiền tệ để tránh sai số
    private int quantity;
    private int productId; // ID của sản phẩm trong giỏ hàng
}