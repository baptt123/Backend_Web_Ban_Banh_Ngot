package com.example.myappbackend.service.interfaceservice;

import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoriesService {
    List<CategoryWithImageDTO> getCategoriesWithImage();
}
