// PaymentController.java
package com.soko_backend.controller;

import com.soko_backend.dto.payment.CreatePaymentIntentRequest;
import com.soko_backend.enums.OrderStatus;
import com.soko_backend.repository.OrderRepository;
import com.soko_backend.service.payment.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Paiement", description = "Gestion des paiements Stripe")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    @Operation(summary = "Créer un PaymentIntent Stripe pour une commande")
    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody CreatePaymentIntentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentIntent(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
        try {
            String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            String sigHeader = request.getHeader("Stripe-Signature");

            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("🔔 Webhook Stripe reçu : " + event.getType());

            if ("payment_intent.succeeded".equals(event.getType()) ||
                    "payment_intent.payment_failed".equals(event.getType())) {

                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);

                if (intent != null && intent.getDescription() != null) {
                    String description = intent.getDescription();
                    Long orderId = Long.valueOf(description.replaceAll("[^0-9]", ""));
                    System.out.println("🛒 Traitement du paiement pour commande #" + orderId);

                    orderRepository.findById(orderId).ifPresent(order -> {
                        if ("payment_intent.succeeded".equals(event.getType())) {
                            System.out.println("✅ Paiement confirmé pour la commande #" + orderId);
                            order.setStatus(OrderStatus.PAID);
                        } else if ("payment_intent.payment_failed".equals(event.getType())) {
                            System.out.println("❌ Paiement échoué pour la commande #" + orderId);
                            order.setStatus(OrderStatus.FAILED);
                        }
                        orderRepository.save(order);
                        System.out.println("💾 Statut de la commande mis à jour en base : " + order.getStatus());
                    });
                } else {
                    System.out.println("⚠️ Aucun intent ou description dans le webhook reçu.");
                }
            }

            return ResponseEntity.ok("Webhook reçu et traité");

        } catch (Exception e) {
            System.out.println("🚨 Erreur dans le traitement du webhook : " + e.getMessage());
            return ResponseEntity.badRequest().body("Erreur traitement Stripe webhook: " + e.getMessage());
        }
    }
}