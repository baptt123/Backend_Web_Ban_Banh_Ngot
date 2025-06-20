package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.request.CategoryRequest;
import com.example.myappbackend.dto.response.CategoryResponse;
import com.example.myappbackend.model.Category;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.repository.CategoriesRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.service.interfaceservice.StoreCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreCategoryServiceImpl implements StoreCategoryService {

    private final CategoriesRepository categoriesRepository;
    private final StoreRepository storeRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoriesRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDeleted(0);
        return mapToResponse(categoriesRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Integer categoryId, CategoryRequest request) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to your store"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return mapToResponse(categoriesRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to your store"));

        category.setDeleted(1);
        categoriesRepository.save(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getCategoryId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        return response;
    }
}

