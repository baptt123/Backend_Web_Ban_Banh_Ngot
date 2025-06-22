package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.CategoryRequestDTO;
import com.example.myappbackend.dto.DTO.CategoryResponseDTO;
import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.model.Category;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.repository.CategoriesRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.service.interfaceservice.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private StoreRepository storeRepository;


    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public void createCategory(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setDeleted(0);

        if (dto.getStoreId() != null) {
            Stores store = storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store không tồn tại"));
            category.setStore(store);
        }

        categoriesRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (!categoriesRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy danh mục với id: " + id);
        }
        categoriesRepository.deleteById(id);
    }

    @Override
    public List<CategoryWithImageDTO> getCategoriesWithImage() {
        return categoriesRepository.findCategoriesWithOneProductImage();
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoriesRepository.findByDeleted(0);
        return categories.stream()
                .map(cat -> new CategoryResponseDTO(
                        cat.getCategoryId(),
                        cat.getName(),
                        cat.getDescription(),
                        cat.getStore() != null ? cat.getStore().getName() : "Không rõ"
                ))
                .collect(Collectors.toList());
    }
}
