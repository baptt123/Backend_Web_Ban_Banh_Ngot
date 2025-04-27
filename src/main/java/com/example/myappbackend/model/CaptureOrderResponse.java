package com.example.myappbackend.model;

import lombok.Data;

import java.util.List;

@Data
public class CaptureOrderResponse {
    private String id;
    private String status;
    private PaymentSource payment_source;
    private List<PurchaseUnit> purchase_units;
    private Payer payer;
    private List<Link> links;
}