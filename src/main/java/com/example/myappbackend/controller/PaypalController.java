package com.example.myappbackend.controller;

import com.example.myappbackend.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;

    @PostMapping("/create-paypal-order")
    public Object createOrder() {
        try {
            String value = "50000";
            String currency = "USD";
            String reference = "DH" + System.currentTimeMillis();
            return paypalService.createOrder(value, currency, reference);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/capture-paypal-order")
    public Object captureOrder(@RequestParam String orderId) {
        try {
            return paypalService.captureOrder(orderId);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}