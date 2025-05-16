package com.cupabakery.backend.model;

import lombok.Data;

@Data
public class Payer {
    private Name name;
    private String email_address;
    private String payer_id;
}