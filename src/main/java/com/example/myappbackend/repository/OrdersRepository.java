package com.example.myappbackend.repository;

import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.model.OrderStatus;
import com.example.myappbackend.model.Orders;
import com.example.myappbackend.model.Stores;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByStore(Stores store);

    List<Orders> findByStore_StoreId(Integer storeId);

    @Query("SELECT SUM(o.totalAmount), COUNT(o) FROM Orders o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status = :status" +
            " AND o.store.storeId = :storeId")
    List<Object[]> calculateRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatus status,
            @Param("storeId") Integer storeId
    );

    @Query("SELECT new com.example.myappbackend.dto.DTO.ProductRevenueDTO(p.productId, p.name, SUM(od.price * od.quantity), SUM(od.quantity)) " +
            "FROM Orders o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status = :status " +
            "AND o.store.storeId = :storeId " +
            "GROUP BY p.productId, p.name")
    List<ProductRevenueDTO> getRevenueByProducts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatus status,
            @Param("storeId") Integer storeId
    );


    // --- Thêm phương thức này để tìm đơn hàng theo PayPal Order ID ---
    Optional<Orders> findByPaypalOrderId(String paypalOrderId);
    // -----------------------------------------------------------------

    // Giữ lại phương thức này nếu bạn vẫn cần nó cho mục đích test khác
    Optional<Orders> findTopByStatusOrderByCreatedAtDesc(OrderStatus status);

    @Query("UPDATE Orders o SET o.deleted = :deleted WHERE o.orderId = :orderId")
    void updateOrderDeletedStatus(@Param("orderId") Integer orderId, @Param("deleted") boolean deleted);
}
