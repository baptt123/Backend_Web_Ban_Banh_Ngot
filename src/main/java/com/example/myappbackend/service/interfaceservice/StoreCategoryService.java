package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.request.CategoryRequest;
import com.example.myappbackend.dto.response.CategoryResponse;

import java.util.List;

public interface StoreCategoryService {
    List<CategoryResponse> getAllCategoriesByStore(Integer storeId);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Integer id, CategoryRequest request);
    void deleteCategory(Integer id);
}
