package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.CategoryRequest;
import com.example.myappbackend.dto.response.CategoryResponse;

import java.util.List;

public interface StoreCategoryService {
    List<CategoryResponse> getAllCategoriesByStore(Integer storeId);
    CategoryResponse createCategory(Integer storeId, CategoryRequest request);
    CategoryResponse updateCategory(Integer storeId, Integer categoryId, CategoryRequest request);
    void deleteCategory(Integer storeId, Integer categoryId);
}
