package com.example.myappbackend.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Cho phép frontend gọi từ domain khác
public class UploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Tạo tên file và đường dẫn lưu
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Ghi file vào thư mục
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Trả về URL (tuỳ chỉnh tùy theo cách bạn serve ảnh)
            String fileUrl = "/uploads/" + fileName;
            return ResponseEntity.ok().body(new UploadResponse(fileUrl));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    // Lớp phản hồi trả về JSON
    @Getter
    public static class UploadResponse {
        private String url;
        public UploadResponse(String url) { this.url = url; }
    }
}