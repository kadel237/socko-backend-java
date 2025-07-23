package com.soko_backend.dto.cart;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private Long productId;
    private int quantity;
}