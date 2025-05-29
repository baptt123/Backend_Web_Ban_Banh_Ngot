package com.example.myappbackend.dto.response;

import com.example.myappbackend.model.Link;
import com.example.myappbackend.model.Payer;
import com.example.myappbackend.model.PaymentSource;
import com.example.myappbackend.model.PurchaseUnit;
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