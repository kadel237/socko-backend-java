package com.soko_backend.dto.order;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private String status; // Ex: "PENDING", "VALIDATED", "SHIPPED"
}
