package com.example.myappbackend.repository;

import com.example.myappbackend.model.Comment;
import com.example.myappbackend.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByProductAndDeletedFalseOrderByCreatedAtDesc(Products product);
    List<Comment> findByDeletedFalseOrderByCreatedAtDesc();
}
