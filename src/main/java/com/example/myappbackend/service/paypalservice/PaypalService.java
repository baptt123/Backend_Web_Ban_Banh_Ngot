package com.example.myappbackend.service.paypalservice;

import com.example.myappbackend.dto.request.CreateOrderRequest;
import com.example.myappbackend.dto.response.AuthResponse;
import com.example.myappbackend.dto.response.CaptureOrderResponse;
import com.example.myappbackend.dto.response.CreateOrderResponse;
import com.example.myappbackend.model.Amount;
import com.example.myappbackend.model.PurchaseUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;


@Service
public class PaypalService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode="Sandbox"; // or "Live"

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "Live".equalsIgnoreCase(mode) ?
                "https://api-m.paypal.com" :
                "https://api-m.sandbox.paypal.com";
    }

    private AuthResponse authenticate() {
        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(credentials);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                getBaseUrl() + "/v1/oauth2/token",
                HttpMethod.POST,
                request,
                AuthResponse.class
        );

        return response.getBody();
    }

    public CreateOrderResponse createOrder(String value, String currency, String reference) {
        AuthResponse auth = authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(auth.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateOrderRequest requestBody = new CreateOrderRequest();
        requestBody.setIntent("CAPTURE");
        PurchaseUnit unit = new PurchaseUnit(reference, new Amount(currency, value));
        requestBody.setPurchase_units(List.of(unit));

        HttpEntity<CreateOrderRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<CreateOrderResponse> response = restTemplate.exchange(
                getBaseUrl() + "/v2/checkout/orders",
                HttpMethod.POST,
                request,
                CreateOrderResponse.class
        );

        return response.getBody();
    }

    public CaptureOrderResponse captureOrder(String orderId) {
        AuthResponse auth = authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(auth.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        ResponseEntity<CaptureOrderResponse> response = restTemplate.exchange(
                getBaseUrl() + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                request,
                CaptureOrderResponse.class
        );

        return response.getBody();
    }
}
