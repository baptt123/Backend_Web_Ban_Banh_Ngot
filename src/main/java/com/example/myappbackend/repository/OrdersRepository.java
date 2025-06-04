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

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByStore(Stores store);
    List<Orders> findByStoreStoreId(Integer storeId);
    @Query("SELECT SUM(o.totalAmount), COUNT(o) FROM Orders o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "AND o.status = :status")
    List<Object[]> calculateRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatus status
    );

    @Query("SELECT new com.example.myappbackend.dto.DTO.ProductRevenueDTO(p.productId, p.name, SUM(od.price * od.quantity), SUM(od.quantity)) " +
            "FROM Orders o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "WHERE o.createdAt BETWEEN ?1 AND ?2 " +
            "AND o.status = ?3 " +
            "GROUP BY p.productId, p.name")
    List<ProductRevenueDTO> getRevenueByProducts(LocalDateTime startDate, LocalDateTime endDate, OrderStatus status);


}
