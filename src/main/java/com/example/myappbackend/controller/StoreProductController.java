package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.StoreDTO;
import com.example.myappbackend.dto.request.ProductRequest;
import com.example.myappbackend.dto.response.ProductResponse;
import com.example.myappbackend.model.Products;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.ProductRepository;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.impl.StoreProductServiceImpl;
import com.example.myappbackend.service.interfaceservice.StoreProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.JwaAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/store/products")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StoreProductController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final StoreProductService storeProductService;
    @Autowired
    private final StoreRepository storeRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final StoreProductServiceImpl storeProductServiceImpl;

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/all-products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(HttpServletRequest request) {
        User user = getUserFromRequest(request);

        Stores store = storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng của người quản lý"));

        List<ProductResponse> products = storeProductService.getAllProductsByStore(store.getStoreId());

        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeProductService.createProduct(request));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id,
                                                         @RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeProductService.updateProduct(id, request));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        storeProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/my-store")
    public ResponseEntity<?> getMyStore(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().getName().equals("MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền truy cập cửa hàng");
        }

        Stores store = storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        StoreDTO storeDTO = storeProductServiceImpl.convertToDTO(store);
        return ResponseEntity.ok(storeDTO);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
