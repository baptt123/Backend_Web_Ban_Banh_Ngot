package com.example.myappbackend.controller;

import com.example.myappbackend.dto.DTO.CreateOrderRequestDTO;
import com.example.myappbackend.dto.DTO.CartItemDTO;
import com.example.myappbackend.dto.response.CaptureOrderResponse;
import com.example.myappbackend.dto.response.CreateOrderResponse;
import com.example.myappbackend.model.*;
import com.example.myappbackend.repository.*;
import com.example.myappbackend.service.paypalservice.PaypalService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin({"http://localhost:5173", "http://localhost:3000"})
public class PaypalController {

    private final PaypalService paypalService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository orderRepository;
    // Removed UserRepository and StoreRepository injection for simpler testing

    public PaypalController(PaypalService paypalService,
                            OrdersRepository orderRepository,
                            OrderDetailsRepository orderDetailsRepository,
                            ProductRepository productRepository) {
        this.paypalService = paypalService;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/create-paypal-order")
    @Transactional
    public ResponseEntity<?> createPaypalOrder(@RequestBody CreateOrderRequestDTO request) {
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

                BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
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

            // === Create and Save Order in DB with PENDING status ===
            Orders newOrder = new Orders();
            Stores store = new Stores();
            store.setStoreId(1);
            User user=new User();
            user.setUserId(1);
            user.setStore(store);
            // --- MODIFICATION FOR TESTING: Set User and Store to null ---
//            newOrder.setUser(null); // Or set a dummy user if your DB doesn't allow null

            newOrder.setStore(store); // Or set a dummy store if your DB doesn't allow null
            // -------------------------------------------------------------
            newOrder.setTotalAmount(finalCalculatedTotalVND);
            newOrder.setStatus(OrderStatus.PENDING);
            newOrder.setPaymentMethod(PaymentMethod.ONLINE);
            newOrder.setCreatedAt(LocalDateTime.now());
            newOrder.setUpdatedAt(LocalDateTime.now());
            newOrder.setUser(user);
            Orders savedOrder = orderRepository.save(newOrder);

            for (OrderDetails detail : orderDetailsList) {
                detail.setOrder(savedOrder);
                orderDetailsRepository.save(detail);
            }

            String referenceId = "ORDER-" + savedOrder.getOrderId();

            CreateOrderResponse paypalResponse = paypalService.createOrder(totalAmountUSD.toString(), currency, referenceId);

            // Save the PayPal Order ID back to your internal order
            savedOrder.setPaypalOrderId(paypalResponse.getId());
            orderRepository.save(savedOrder);

            System.out.println("PayPal Order ID: " + paypalResponse.getId() + " for internal order ID: " + savedOrder.getOrderId());

            return ResponseEntity.ok(paypalResponse);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating PayPal order: " + ex.getMessage()));
        }
    }

    @PostMapping("/capture-paypal-order")
    @Transactional
    public ResponseEntity<?> capturePaypalOrder(@RequestParam("orderId") String paypalOrderID) {
        try {
            CaptureOrderResponse captureResponse = paypalService.captureOrder(paypalOrderID);

            // Find the order by the PayPal Order ID
            // Make sure you have `findByPaypalOrderId` in your OrderRepository
            Orders orderToUpdate = orderRepository.findByPaypalOrderId(paypalOrderID)
                    .orElseThrow(() -> new RuntimeException("Order not found with PayPal ID: " + paypalOrderID));

            // Update order status to DELIVERED (or COMPLETED)
            orderToUpdate.setStatus(OrderStatus.DELIVERED);
            orderToUpdate.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(orderToUpdate);

            return ResponseEntity.ok(captureResponse);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Error capturing PayPal order: " + ex.getMessage()));
        }
    }
}