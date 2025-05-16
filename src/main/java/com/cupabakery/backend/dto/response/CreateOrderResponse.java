package com.cupabakery.backend.dto.response;

import com.cupabakery.backend.model.Link;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderResponse {
    private String id;
    private String status;
    private List<Link> links;
}