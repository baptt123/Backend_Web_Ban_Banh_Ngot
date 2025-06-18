package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.dto.response.StoreResponse;

import java.util.List;

public interface StoreProductService {
    List<ProductResponse> getAllProductsByStore(Integer storeId);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Integer productId, ProductRequest request);
    void deleteProduct(Integer productId);
    List<StoreResponse> getAllStores();
}
