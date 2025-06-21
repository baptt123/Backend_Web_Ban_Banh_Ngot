package com.example.myappbackend.dto.orderDTO;

import com.example.myappbackend.dto.DTO.CartItemDTO;
import lombok.Data;

import java.util.List;
@Data
public class CreateOrderRequestPaypal {
    private String address;
    private String phone;
    private String email;
    private String paymentMethod;
    private String promotionCode;
    private List<CartItemDTO> items;
}
