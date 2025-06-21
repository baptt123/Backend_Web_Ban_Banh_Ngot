package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CartItemDTO;
import com.example.myappbackend.dto.orderDTO.CreateOrderRequestPaypal;
import com.example.myappbackend.dto.response.CaptureOrderResponse;
import com.example.myappbackend.dto.response.CreateOrderResponse;
import com.example.myappbackend.model.*;
import com.example.myappbackend.repository.*;
import com.example.myappbackend.service.impl.JwtService;
import com.example.myappbackend.service.paypalservice.PaypalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin({"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class PaypalController {
    @Autowired
    private final PaypalService paypalService;
    @Autowired
    private final OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final OrdersRepository orderRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PromotionRepository promotionRepo;
    @PostMapping("/create-paypal-order")
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMER')")
    public ResponseEntity<?> createPaypalOrder(@RequestBody CreateOrderRequestPaypal request, HttpServletRequest httpRequest) {
        List<CartItemDTO> items = request.getItems();
        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().body("Không có sản phẩm trong đơn hàng.");
        }

        BigDecimal totalVND = BigDecimal.ZERO;
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (CartItemDTO item : items) {
            Products product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + item.getProductId()));

            if (item.getQuantity() > product.getStock()) {
                return ResponseEntity.badRequest().body("Sản phẩm " + product.getName() + " không đủ hàng.");
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalVND = totalVND.add(itemTotal);

            OrderDetails detail = new OrderDetails();
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice());
            detail.setCustomization(item.getCustomization());
            orderDetailsList.add(detail);
        }

        // Thuế và phí ship
        BigDecimal shipping = totalVND.multiply(new BigDecimal("0.1"));
        BigDecimal tax = totalVND.multiply(new BigDecimal("0.08"));
        BigDecimal totalWithFees = totalVND.add(shipping).add(tax);

        // ===== 📌 Áp dụng khuyến mãi nếu có =====
        if (request.getPromotionCode() != null && !request.getPromotionCode().isBlank()) {
            Promotions promo = promotionRepo.findByName(request.getPromotionCode())
                    .filter(p -> {
                        LocalDateTime now = LocalDateTime.now();
                        return p.getStartDate() != null && p.getEndDate() != null
                                && now.isAfter(p.getStartDate()) && now.isBefore(p.getEndDate());
                    })
                    .orElseThrow(() -> new RuntimeException("Mã khuyến mãi không hợp lệ hoặc đã hết hạn."));

            BigDecimal discount = totalWithFees.multiply(promo.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
            totalWithFees = totalWithFees.subtract(discount);
        }

        BigDecimal usdRate = new BigDecimal("25000");
        BigDecimal totalUSD = totalWithFees.divide(usdRate, 2, RoundingMode.HALF_UP);

        // Lấy user từ token cookie
        String token = jwtService.getTokenFromCookies(httpRequest);
        User user = jwtService.extractUserFromToken(token);

        Orders order = new Orders();
        order.setUser(user);
        order.setStore(orderDetailsList.get(0).getProduct().getStore());
        order.setTotalAmount(totalWithFees);
        order.setPaymentMethod(PaymentMethod.ONLINE);
        order.setStatus(OrderStatus.PENDING);
        order.setAddress(request.getAddress());
        order.setPhone(request.getPhone());
        order.setEmail(request.getEmail());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDeleted(false);
        Orders saved = orderRepository.save(order);

        for (OrderDetails od : orderDetailsList) {
            od.setOrder(saved);
            orderDetailsRepository.save(od);
        }

        String referenceId = "ORDER-" + saved.getOrderId();
        CreateOrderResponse paypalResponse = paypalService.createOrder(totalUSD.toString(), "USD", referenceId);
        saved.setPaypalOrderId(paypalResponse.getId());
        orderRepository.save(saved);

        return ResponseEntity.ok(paypalResponse);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('CUSTOMER')") // cần xác thực mới capture
    @PostMapping("/capture-paypal-order")
    @Transactional
    public ResponseEntity<?> capturePaypalOrder(@RequestParam("orderId") String paypalOrderID) {
        try {
            CaptureOrderResponse captureResponse = paypalService.captureOrder(paypalOrderID);

            Orders order = orderRepository.findByPaypalOrderId(paypalOrderID)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID PayPal: " + paypalOrderID));

            order.setStatus(OrderStatus.DELIVERED); // cập nhật trạng thái
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            for (OrderDetails detail : order.getOrderDetails()) {
                Products product = detail.getProduct();
                product.setStock(product.getStock() - detail.getQuantity());
                productRepository.save(product);
            }

            return ResponseEntity.ok(captureResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi xác nhận PayPal: " + e.getMessage()));
        }
    }
}
