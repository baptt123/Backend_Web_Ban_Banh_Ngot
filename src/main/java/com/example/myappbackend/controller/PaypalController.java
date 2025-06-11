package com.example.myappbackend.controller;

import com.example.myappbackend.dto.response.CaptureOrderResponse;
import com.example.myappbackend.dto.response.CreateOrderResponse;
import com.example.myappbackend.service.paypalservice.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin({"http://localhost:5173", "http://localhost:3000"})
public class PaypalController {
    @Autowired
    private final PaypalService paypalService;

    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @PostMapping("/create-paypal-order")
    public ResponseEntity<?> createPaypalOrder() {
        String totalAmount = "50000";
        String currency = "USD";
        String referenceId = "DH" + System.currentTimeMillis();

        try {
            CreateOrderResponse response = paypalService.createOrder(totalAmount, currency, referenceId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/capture-paypal-order")
    public ResponseEntity<?> capturePaypalOrder(@RequestParam("orderId") String orderID) {
        try {
            CaptureOrderResponse response = paypalService.captureOrder(orderID);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}
