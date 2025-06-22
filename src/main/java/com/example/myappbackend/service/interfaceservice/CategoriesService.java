package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.CategoryRequestDTO;
import com.example.myappbackend.dto.DTO.CategoryResponseDTO;
import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoriesService {
    void createCategory(CategoryRequestDTO dto);
    void deleteCategory(Integer id);
    List<CategoryWithImageDTO> getCategoriesWithImage();
    List<CategoryResponseDTO> getAllCategories();
}
