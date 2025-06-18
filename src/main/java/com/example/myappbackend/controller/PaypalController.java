package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CartItemDTO;
import com.example.myappbackend.dto.DTO.CreateOrderRequestDTO;
import com.example.myappbackend.dto.response.CaptureOrderResponse;
import com.example.myappbackend.dto.response.CreateOrderResponse;
import com.example.myappbackend.model.*;
import com.example.myappbackend.repository.*;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.paypalservice.PaypalService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin({"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypalService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository orderRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    @PostMapping("/create-paypal-order")
    @Transactional
    public ResponseEntity<?> createPaypalOrder(@RequestBody CreateOrderRequestDTO request, HttpServletRequest httpServletRequest) {
        BigDecimal recalculatedTotalVND = BigDecimal.ZERO;
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        String currency = "USD";

        try {
            for (CartItemDTO item : request.getCartList()) {
                Optional<Products> productOptional = productRepository.findById(item.getProductId());
                if (productOptional.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Product not found with ID: " + item.getProductId()));
                }
                Products product = productOptional.get();

                if (product.getPrice().compareTo(item.getPrice()) != 0) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Price mismatch for product: " + product.getName()));
                }

                if (item.getQuantity() > product.getStock()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Insufficient stock for product: " + product.getName()));
                }

                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                recalculatedTotalVND = recalculatedTotalVND.add(itemTotal);

                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setProduct(product);
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setPrice(product.getPrice());
                orderDetail.setCustomization("");
                orderDetailsList.add(orderDetail);
            }

            BigDecimal shippingRate = new BigDecimal("0.10");
            BigDecimal taxRate = new BigDecimal("0.08");

            BigDecimal calculatedShipping = recalculatedTotalVND.multiply(shippingRate);
            BigDecimal calculatedTax = recalculatedTotalVND.multiply(taxRate);
            BigDecimal finalCalculatedTotalVND = recalculatedTotalVND.add(calculatedShipping).add(calculatedTax);

            BigDecimal exchangeRate = new BigDecimal("25000");
            BigDecimal totalAmountUSD = finalCalculatedTotalVND.divide(exchangeRate, 2, RoundingMode.HALF_UP);

            String token = Arrays.stream(httpServletRequest.getCookies())
                    .filter(c -> c.getName().equals("access_token"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Token not found"));

            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Orders newOrder = new Orders();
            newOrder.setStore(orderDetailsList.get(0).getProduct().getStore()); // Lấy store từ product đầu tiên
            newOrder.setUser(user);
            newOrder.setTotalAmount(finalCalculatedTotalVND);
            newOrder.setStatus(OrderStatus.PENDING);
            newOrder.setPaymentMethod(PaymentMethod.ONLINE);
            newOrder.setCreatedAt(LocalDateTime.now());
            newOrder.setUpdatedAt(LocalDateTime.now());

            Orders savedOrder = orderRepository.save(newOrder);

            for (OrderDetails detail : orderDetailsList) {
                detail.setOrder(savedOrder);
                orderDetailsRepository.save(detail);
            }

            String referenceId = "ORDER-" + savedOrder.getOrderId();

            CreateOrderResponse paypalResponse = paypalService.createOrder(totalAmountUSD.toString(), currency, referenceId);

            savedOrder.setPaypalOrderId(paypalResponse.getId());
            orderRepository.save(savedOrder);

            return ResponseEntity.ok(paypalResponse);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating PayPal order: " + ex.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    @PostMapping("/capture-paypal-order")
    @Transactional
    public ResponseEntity<?> capturePaypalOrder(@RequestParam("orderId") String paypalOrderID) {
        try {
            CaptureOrderResponse captureResponse = paypalService.captureOrder(paypalOrderID);

            Orders orderToUpdate = orderRepository.findByPaypalOrderId(paypalOrderID)
                    .orElseThrow(() -> new RuntimeException("Order not found with PayPal ID: " + paypalOrderID));

            orderToUpdate.setStatus(OrderStatus.DELIVERED);
            orderToUpdate.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(orderToUpdate);

            // Trừ tồn kho sau khi thanh toán thành công
            for (OrderDetails detail : orderToUpdate.getOrderDetails()) {
                Products product = detail.getProduct();
                int updatedStock = product.getStock() - detail.getQuantity();
                product.setStock(updatedStock);
                productRepository.save(product);
            }

            return ResponseEntity.ok(captureResponse);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Error capturing PayPal order: " + ex.getMessage()));
        }
    }
}
