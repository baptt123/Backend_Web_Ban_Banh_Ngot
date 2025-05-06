package com.example.myappbackend.controller;

import com.example.myappbackend.service.impl.PaypalService;
import com.example.myappbackend.service.impl.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;
    private final ExchangeRateService exchangeRateService;

    @PostMapping("/create-paypal-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
        try {
            if (!payload.containsKey("total")) {
                return ResponseEntity.badRequest().body("Thiếu thông tin tổng tiền");
            }

            double vndAmount = Double.parseDouble(payload.get("total").toString());
            double usdAmount = exchangeRateService.convertVndToUsd(vndAmount);
            usdAmount = Math.round(usdAmount * 100.0) / 100.0;
            
            if (usdAmount <= 0) {
                return ResponseEntity.badRequest().body("Số tiền không hợp lệ");
            }

            String currency = "USD";
            String reference = "DH" + System.currentTimeMillis();
            
            Object paypalResponse = paypalService.createOrder(String.valueOf(usdAmount), currency, reference);
            
            Map<String, Object> response = new HashMap<>();
            response.put("order", paypalResponse);
            response.put("exchangeRate", exchangeRateService.getCurrentRate());
            
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Số tiền không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi tạo đơn PayPal: " + e.getMessage());
        }
    }

    @PostMapping("/capture-paypal-order")
    public ResponseEntity<?> captureOrder(@RequestParam String orderId) {
        try {
            Object captureResponse = paypalService.captureOrder(orderId);
            return ResponseEntity.ok(captureResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi khi capture đơn PayPal: " + e.getMessage());
        }
    }

    @GetMapping("/exchange-rate")
    public ResponseEntity<Map<String, Object>> getExchangeRate() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("rate", exchangeRateService.getCurrentRate());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}