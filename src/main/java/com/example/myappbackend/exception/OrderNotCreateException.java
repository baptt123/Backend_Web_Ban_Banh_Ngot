package com.example.myappbackend.exception;

public class OrderNotCreateException extends RuntimeException {
    public OrderNotCreateException(String message) {
        super(message);
    }
}
