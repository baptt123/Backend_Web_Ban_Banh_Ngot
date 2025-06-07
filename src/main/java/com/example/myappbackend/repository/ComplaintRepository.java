package com.example.myappbackend.repository;

import com.example.myappbackend.model.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaints, Integer> {
}
