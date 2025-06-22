package com.example.myappbackend.controller;

import com.example.myappbackend.dto.response.StoreResponse;
import com.example.myappbackend.service.interfaceservice.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/list-store")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class StoreControlller {

    private final StoreProductService storeProductService;
    @GetMapping("/all")
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> stores = storeProductService.getAllStores();
        return ResponseEntity.ok(stores);
    }
}
