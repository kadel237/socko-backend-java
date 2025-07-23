package com.soko_backend.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private Long customerId; // l'utilisateur connecté qui passe la commande

    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}