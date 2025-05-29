package com.example.myappbackend.repository;


import com.example.myappbackend.model.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Stores, Integer> {
}
