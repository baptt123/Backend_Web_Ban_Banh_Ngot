package com.cupabakery.backend.dto.request;

import com.cupabakery.backend.model.PurchaseUnit;
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