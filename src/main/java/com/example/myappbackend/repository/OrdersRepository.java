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

    @Query(value = """
    SELECT DATE(o.created_at) AS date, SUM(o.total_amount) AS revenue
    FROM orders o
    WHERE o.store_id = :storeId
      AND o.created_at BETWEEN :startDate AND :endDate
      AND o.status IN ('CONFIRMED', 'SHIPPED', 'DELIVERED')
      AND o.deleted = 0
    GROUP BY DATE(o.created_at)
    ORDER BY DATE(o.created_at)
""", nativeQuery = true)
    List<Object[]> findRevenueByStoreAndDateRange(
            @Param("storeId") Integer storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );



    @Query("SELECT c.name, SUM(od.price * od.quantity) as revenue " +
            "FROM Orders o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "JOIN p.category c " +
            "WHERE o.store.storeId= :storeId " +
            "AND o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status = 'DELIVERED' " +
            "AND c.deleted = 0 " +
            "GROUP BY c.name")
    List<Object[]> findRevenueByCategoryForMonth(@Param("storeId") Integer storeId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);


    // --- Thêm phương thức này để tìm đơn hàng theo PayPal Order ID ---
    Optional<Orders> findByPaypalOrderId(String paypalOrderId);
    // -----------------------------------------------------------------

    // Giữ lại phương thức này nếu bạn vẫn cần nó cho mục đích test khác
    Optional<Orders> findTopByStatusOrderByCreatedAtDesc(OrderStatus status);

    @Query("UPDATE Orders o SET o.deleted = :deleted WHERE o.orderId = :orderId")
    void updateOrderDeletedStatus(@Param("orderId") Integer orderId, @Param("deleted") boolean deleted);
}
