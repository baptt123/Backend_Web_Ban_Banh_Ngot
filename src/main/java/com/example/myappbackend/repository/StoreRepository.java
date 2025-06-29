package com.example.myappbackend.repository;


import com.example.myappbackend.model.Stores;
import com.example.myappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Stores, Integer> {
    Optional<Stores> findByManager(User manager);
    Optional<Stores> findByAddress(String address);
}
