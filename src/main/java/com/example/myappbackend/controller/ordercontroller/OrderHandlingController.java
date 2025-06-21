// Controller
package com.example.myappbackend.controller.ordercontroller;

import com.example.myappbackend.dto.orderDTO.OrderRequest;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.orderservice.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders-handle")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class OrderHandlingController {
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtUtil;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER') or hasAuthority('MANAGER')")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest req, HttpServletRequest request) {
        String jwt = jwtUtil.getTokenFromCookies(request);
        return ResponseEntity.ok(orderService.placeOrder(jwt, req));
    }
}