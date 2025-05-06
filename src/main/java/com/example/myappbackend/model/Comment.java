package com.example.myappbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String createdAt;
}
