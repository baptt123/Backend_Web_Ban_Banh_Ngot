package com.example.myappbackend.repository;

import com.example.myappbackend.model.Orders;
import com.example.myappbackend.model.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByStore(Stores store);
    @Query("SELECT DATE(o.createdAt) as date, SUM(o.totalAmount) " +
            "FROM Orders o WHERE o.store.storeId = :storeId AND o.createdAt BETWEEN :start AND :end " +
            "GROUP BY DATE(o.createdAt) ORDER BY DATE(o.createdAt)")
    List<Object[]> getDailyRevenue(Integer storeId, LocalDate start, LocalDate end);

    @Query("SELECT FUNCTION('WEEK', o.createdAt) as week, SUM(o.totalAmount) " +
            "FROM Orders o WHERE o.store.storeId = :storeId AND FUNCTION('YEAR', o.createdAt) = :year " +
            "GROUP BY FUNCTION('WEEK', o.createdAt) ORDER BY week")
    List<Object[]> getWeeklyRevenue(Integer storeId, Integer year);

    @Query("SELECT FUNCTION('MONTH', o.createdAt) as month, SUM(o.totalAmount) " +
            "FROM Orders o WHERE o.store.storeId = :storeId AND FUNCTION('YEAR', o.createdAt) = :year " +
            "GROUP BY FUNCTION('MONTH', o.createdAt) ORDER BY month")
    List<Object[]> getMonthlyRevenue(Integer storeId, Integer year);
    List<Orders> findByStore_StoreId(Integer storeId);
}
