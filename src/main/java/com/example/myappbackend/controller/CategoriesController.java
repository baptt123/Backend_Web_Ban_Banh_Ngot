package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.service.interfaceservice.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    // Phương thức lấy toàn bộ danh sách danh mục với hình ảnh
    @GetMapping
    public ResponseEntity<?> getAllCategoriesWithImage() {
        try {
            List<CategoryWithImageDTO> list = categoriesService.getCategoriesWithImage();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy danh mục: " + e.getMessage());
        }
    }
}
