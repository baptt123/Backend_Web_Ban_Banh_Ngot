package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.OrderRequestDTO;
import com.example.myappbackend.dto.DTO.OrderResponseDTO;
import com.example.myappbackend.dto.request.OrderRequest;
import com.example.myappbackend.dto.response.OrderResponse;
import com.example.myappbackend.exception.OrderNotCreateException;
import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.StoreRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.impl.OrderServiceImpl;
import com.example.myappbackend.service.interfaceservice.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMER')")
    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        try {
            OrderResponse response = orderService.placeOrder(request);
            return ResponseEntity.ok(response);
        } catch (OrderNotCreateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + ex.getMessage());
        }
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/getallordersbystore")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(HttpServletRequest request) {
        Integer storeID= getStoreIdFromRequest(request);
        return ResponseEntity.ok(orderService.getAllOrdersByStore(storeID));
    }



//    @PutMapping("/{id}")
//    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Integer id, @RequestBody OrderRequestDTO request) {
//        return ResponseEntity.ok(orderService.updateOrder(id, request));
//    }
//    @PreAuthorize("hasAuthority('MANAGER')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
//        orderService.deleteOrderByDeletedStatus(id, true);
//        return ResponseEntity.noContent().build();
//    }
    private Integer getStoreIdFromRequest(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stores store = storeRepository.findByManager(user)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        return store.getStoreId();
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}/update-status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable("id") Integer orderId,
            @RequestBody OrderRequestDTO request
    ) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(orderId, request);
        return ResponseEntity.ok(updatedOrder);
    }
}
