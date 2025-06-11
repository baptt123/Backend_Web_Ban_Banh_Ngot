package com.example.myappbackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class ExchangeRateService {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private double currentRate = 0.000041; // Tỷ giá mặc định

    public ExchangeRateService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        updateExchangeRate(); // Cập nhật tỷ giá khi khởi tạo service
    }

    @Scheduled(fixedRate = 3600000) // Cập nhật mỗi giờ
    public void updateExchangeRate() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode rates = root.get("rates");
            double usdToVnd = rates.get("VND").asDouble();
            currentRate = 1 / usdToVnd; // Chuyển đổi từ USD/VND sang VND/USD
        } catch (Exception e) {
            // Giữ nguyên tỷ giá cũ nếu có lỗi
            System.out.println("Không thể cập nhật tỷ giá: " + e.getMessage());
        }
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public double convertVndToUsd(double vndAmount) {
        return vndAmount * currentRate;
    }
} 