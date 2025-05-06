package com.example.myappbackend.service;

import com.example.myappbackend.config.PaypalProperties;
import com.example.myappbackend.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaypalProperties paypalProperties;

    private String getBaseUrl() {
        return "live".equalsIgnoreCase(paypalProperties.getMode()) ?
                "https://api-m.paypal.com" :
                "https://api-m.sandbox.paypal.com";
    }

    private AuthResponse authenticate() throws Exception {
        String auth = Base64.getEncoder().encodeToString(
                (paypalProperties.getClientId() + ":" + paypalProperties.getClientSecret())
                        .getBytes(StandardCharsets.UTF_8)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/v1/oauth2/token",
                HttpMethod.POST,
                request,
                String.class
        );

        return objectMapper.readValue(response.getBody(), AuthResponse.class);
    }

    public CreateOrderResponse createOrder(String value, String currency, String reference) throws Exception {
        AuthResponse auth = authenticate();

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setIntent("CAPTURE");
        createOrderRequest.setPurchase_units(List.of(
                new PurchaseUnit(reference, new Amount(currency, value))
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(auth.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateOrderRequest> request = new HttpEntity<>(createOrderRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/v2/checkout/orders",
                HttpMethod.POST,
                request,
                String.class
        );

        return objectMapper.readValue(response.getBody(), CreateOrderResponse.class);
    }

    public CaptureOrderResponse captureOrder(String orderId) throws Exception {
        AuthResponse auth = authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(auth.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                request,
                String.class
        );

        return objectMapper.readValue(response.getBody(), CaptureOrderResponse.class);
    }
}