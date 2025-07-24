// PaymentServiceImpl.java
package com.soko_backend.service.payment;

import com.soko_backend.dto.payment.CreatePaymentIntentRequest;
import com.soko_backend.entity.order.Order;
import com.soko_backend.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public Map<String, String> createPaymentIntent(CreatePaymentIntentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (order.getTotalAmount() * 100)) // montant en centimes
                .setCurrency(request.getCurrency())
                .setDescription("Paiement commande #" + order.getId())
                .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du paiement : " + e.getMessage());
        }
    }
}