package com.cupabakery.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResponse {
    private String id;
    private String status;
    private List<Link> links;
}