package com.soko_backend.service.payment;

import com.soko_backend.dto.payment.CreatePaymentIntentRequest;

import java.util.Map;

public interface PaymentService {
    Map<String, String> createPaymentIntent(CreatePaymentIntentRequest request);
}
