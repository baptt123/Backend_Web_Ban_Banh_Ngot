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
    private final StoreRepository storesRepository;

    private static final int DEFAULT_STORE_ID = 1;

    @Override
    public List<CategoryResponse> getAllCategoriesByStore(Integer storeId) {
        return categoriesRepository.findByStore_StoreIdAndDeleted(storeId, 0)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Stores store = storesRepository.findById(DEFAULT_STORE_ID)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Category cat = new Category();
        cat.setName(request.getName());
        cat.setDescription(request.getDescription());
        cat.setStore(store);
        cat.setDeleted(0);

        return mapToResponse(categoriesRepository.save(cat));
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category cat = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        cat.setName(request.getName());
        cat.setDescription(request.getDescription());

        return mapToResponse(categoriesRepository.save(cat));
    }

    @Override
    public void deleteCategory(Integer id) {
        Category cat = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        cat.setDeleted(1);
        categoriesRepository.save(cat);
    }

    private CategoryResponse mapToResponse(Category cat) {
        CategoryResponse res = new CategoryResponse();
        res.setCategoryId(cat.getCategoryId());
        res.setName(cat.getName());
        res.setDescription(cat.getDescription());
        return res;
    }
}
