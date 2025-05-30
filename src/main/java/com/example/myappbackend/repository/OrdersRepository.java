package com.example.myappbackend.repository;

import com.example.myappbackend.model.Orders;
import com.example.myappbackend.model.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByStore(Stores store);

    @Query("SELECT o FROM Orders o WHERE o.store.storeId = :storeId AND o.createdAt BETWEEN :start AND :end")
    List<Orders> findOrdersByStoreAndDateRange(Integer storeId, LocalDateTime start, LocalDateTime end);
}
