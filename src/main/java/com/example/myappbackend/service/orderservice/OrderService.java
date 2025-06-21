package com.example.myappbackend.service.orderservice;

import com.example.myappbackend.dto.orderDTO.OrderItemRequest;
import com.example.myappbackend.dto.orderDTO.OrderRequest;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.*;
import com.example.myappbackend.repository.*;
import com.example.myappbackend.service.impl.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrdersRepository orderRepo;
    @Autowired
    private final OrderDetailsRepository orderDetailRepo;
    @Autowired
    private final ProductRepository productRepo;
    @Autowired
    private final PromotionRepository promotionRepo;
    @Autowired
    private final StoreRepository storeRepo;
    private final JwtService jwtUtil;
    @Autowired
    private final UserRepository userRepo;
    private final String ORS_API_KEY = "5b3ce3597851110001cf624845897bddd6ec412ba154168bbb4d340e";

    public String placeOrder(String token, OrderRequest req) {
        int userId = jwtUtil.extractUserId(token);
        if (userId == 0) throw new ResourceNotFoundException("Bạn chưa đăng nhập");

        Map<Integer, List<OrderItemRequest>> storeMap = new HashMap<>();
        for (OrderItemRequest item : req.getItems()) {
            Products p = productRepo.findById(item.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Sản phẩm không tồn tại"));
            storeMap.computeIfAbsent(p.getStore().getStoreId(), k -> new ArrayList<>()).add(item);
        }

        StringBuilder result = new StringBuilder();
        for (var entry : storeMap.entrySet()) {
            int storeId = entry.getKey();
            List<OrderItemRequest> items = entry.getValue();

            BigDecimal total = BigDecimal.ZERO;
            for (OrderItemRequest item : items) {
                Products p = productRepo.findById(item.getProductId()).orElseThrow();
                if (p.getStock() < item.getQuantity()) throw new RuntimeException("Không đủ hàng cho sản phẩm " + p.getName());
                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                p.setStock(p.getStock() - item.getQuantity());
                productRepo.save(p);
            }

            BigDecimal shippingFee = calculateShippingFee(req.getAddress(), storeRepo.findById(storeId).orElseThrow().getAddress());
            total = total.add(shippingFee);

            if (req.getPromotionCode() != null && !req.getPromotionCode().isEmpty()) {
                Promotions promo = promotionRepo.findByName(req.getPromotionCode())
                        .filter(p -> {
                            LocalDateTime now = LocalDateTime.now();
                            return p.getStartDate() != null && p.getEndDate() != null &&
                                    p.getStartDate().isBefore(now) && p.getEndDate().isAfter(now);
                        })
                        .orElseThrow(() -> new ResourceNotFoundException("Mã khuyến mãi không hợp lệ"));
                BigDecimal discount = total.multiply(promo.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
                total = total.subtract(discount);
            }

            Orders order = new Orders();
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
            order.setUser(user);
            Stores store = storeRepo.findById(storeId).orElseThrow();
            order.setStore(store);
            order.setTotalAmount(total);
            order.setStatus(OrderStatus.PENDING);
            order.setPaymentMethod(PaymentMethod.COD);
            order.setAddress(req.getAddress());
            order.setPhone(req.getPhone());
            order.setEmail(req.getEmail());
            order.setDeleted(false);
            orderRepo.save(order);

            for (OrderItemRequest item : items) {
                Products p = productRepo.findById(item.getProductId()).orElseThrow();
                OrderDetails detail = new OrderDetails();
                detail.setOrder(order);
                detail.setProduct(p);
                detail.setQuantity(item.getQuantity());
                detail.setPrice(p.getPrice());
                detail.setCustomization(item.getCustomization());
                detail.setDeleted(false);
                orderDetailRepo.save(detail);
            }
            result.append("Đặt hàng thành công tại shop: ").append(storeId).append(". ");
        }

        return result.toString();
    }


    private BigDecimal calculateShippingFee(String userAddress, String storeAddress) {
        try {
            URL url = new URL("https://api.openrouteservice.org/v2/directions/driving-car");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", ORS_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Đây là ví dụ cứng, bạn cần geocode địa chỉ thành toạ độ thật bằng Google hoặc ORS geocode API
            String json = "{\"coordinates\":[[106.700981,10.77689],[106.67563,10.762622]]}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject jsonResponse = new JSONObject(response);
            double distanceInMeters = jsonResponse.getJSONArray("features")
                    .getJSONObject(0).getJSONObject("properties")
                    .getJSONObject("summary").getDouble("distance");

            return BigDecimal.valueOf(distanceInMeters / 1000 * 5000); // 5k / km
        } catch (Exception e) {
            return BigDecimal.valueOf(20000); // fallback fee
        }
    }
}