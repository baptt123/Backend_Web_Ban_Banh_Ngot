package com.example.myappbackend.controller;

import com.example.myappbackend.dto.response.StoreResponse;
import com.example.myappbackend.service.interfaceservice.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class StoreControlller {

    private final StoreProductService storeProductService;

     @GetMapping("/all")
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> stores = storeProductService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getStoreNameById(@PathVariable Integer id) {
        String storeName = storeProductService.getStoreNameById(id);
        return ResponseEntity.ok(storeName);
    }
}
