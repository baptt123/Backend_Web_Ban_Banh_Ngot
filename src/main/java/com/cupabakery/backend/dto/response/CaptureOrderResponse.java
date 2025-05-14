package com.cupabakery.backend.dto.response;

import com.cupabakery.backend.model.Link;
import com.cupabakery.backend.model.Payer;
import com.cupabakery.backend.model.PaymentSource;
import com.cupabakery.backend.model.PurchaseUnit;
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