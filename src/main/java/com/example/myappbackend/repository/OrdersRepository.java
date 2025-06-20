package com.example.myappbackend.repository;

import com.example.myappbackend.dto.DTO.CategoryRevenueDTO;
import com.example.myappbackend.dto.DTO.ProductRevenueDTO;
import com.example.myappbackend.dto.DTO.RevenueStatisticsDTO;
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
    @Query("SELECT NEW com.example.myappbackend.dto.DTO.RevenueStatisticsDTO(DATE(o.createdAt), SUM(o.totalAmount)) " +
            "FROM Orders o " +
            "WHERE o.store.storeId = :storeId " +
            "AND o.status = 'DELIVERED' " +
            "AND o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.deleted = false " +
            "GROUP BY DATE(o.createdAt) " +
            "ORDER BY DATE(o.createdAt)")
    List<RevenueStatisticsDTO> getRevenueStatistics(
            @Param("storeId") Integer storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT NEW com.example.myappbackend.dto.DTO.CategoryRevenueDTO(" +
            "p.category.name, " +
            "SUM(od.price * od.quantity), " +
            "(SUM(od.price * od.quantity) * 100.0 / (" +
            "    SELECT SUM(od2.price * od2.quantity) " +
            "    FROM OrderDetails od2 " +
            "    JOIN od2.order o2 " +
            "    WHERE o2.store.storeId = :storeId " +
            "    AND YEAR(o2.createdAt) = :year " +
            "    AND MONTH(o2.createdAt) = :month " +
            "    AND o2.status = 'DELIVERED' " +
            "    AND o2.deleted = false" +
            "))" +
            ") " +
            "FROM OrderDetails od " +
            "JOIN od.order o " +
            "JOIN od.product p " +
            "WHERE o.store.storeId = :storeId " +
            "AND YEAR(o.createdAt) = :year " +
            "AND MONTH(o.createdAt) = :month " +
            "AND o.status = 'DELIVERED' " +
            "AND o.deleted = false " +
            "GROUP BY p.category.name")
    List<CategoryRevenueDTO> getCategoryRevenue(
            @Param("storeId") Integer storeId,
            @Param("year") int year,
            @Param("month") int month
    );


    // --- Thêm phương thức này để tìm đơn hàng theo PayPal Order ID ---
    Optional<Orders> findByPaypalOrderId(String paypalOrderId);
    // -----------------------------------------------------------------

    // Giữ lại phương thức này nếu bạn vẫn cần nó cho mục đích test khác
    Optional<Orders> findTopByStatusOrderByCreatedAtDesc(OrderStatus status);

    @Query("UPDATE Orders o SET o.deleted = :deleted WHERE o.orderId = :orderId")
    void updateOrderDeletedStatus(@Param("orderId") Integer orderId, @Param("deleted") boolean deleted);
}
