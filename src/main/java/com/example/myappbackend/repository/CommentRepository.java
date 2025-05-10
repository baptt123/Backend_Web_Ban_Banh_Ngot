package com.example.myappbackend.repository;

import com.example.myappbackend.model.Comment;
import com.example.myappbackend.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByProduct(Products product);
}
