package com.example.myappbackend.dto.request;

import com.example.myappbackend.model.PurchaseUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String intent;
    private List<PurchaseUnit> purchase_units;
}