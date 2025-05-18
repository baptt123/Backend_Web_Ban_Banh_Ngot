package com.example.myappbackend.dto.response;

import com.example.myappbackend.model.Link;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResponse {
    private String id;
    private String status;
    private List<Link> links;
}