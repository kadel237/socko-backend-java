package com.soko_backend.dto.payment;

import lombok.Data;

@Data
public class CreatePaymentIntentRequest {
    private Long orderId;
    private String currency; // Ex: "eur", "usd", "xof"
}