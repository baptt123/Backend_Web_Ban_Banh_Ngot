package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CategoryRequestDTO;
import com.example.myappbackend.dto.DTO.CategoryResponseDTO;
import com.example.myappbackend.dto.DTO.CategoryWithImageDTO;
import com.example.myappbackend.model.Category;
import com.example.myappbackend.service.interfaceservice.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO dto) {
        categoriesService.createCategory(dto);
        return ResponseEntity.ok("Thêm danh mục thành công!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        categoriesService.deleteCategory(id);
        return ResponseEntity.ok("Đã xoá danh mục có id: " + id);
    }

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
