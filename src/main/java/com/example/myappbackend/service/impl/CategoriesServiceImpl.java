package com.example.myappbackend.service.impl;

import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.model.Category;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.repository.CategoriesRepository;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.service.interfaceservice.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public List<CategoryWithImageDTO> getCategoriesWithImage() {
        return categoriesRepository.findCategoriesWithOneProductImage();
    }
}
