package com.example.myappbackend.model;

import lombok.Data;

@Data
public class Paypal {
    private Name name;
    private String email_address;
    private String account_id;
}