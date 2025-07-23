package com.soko_backend.controller;

import com.soko_backend.dto.order.CreateOrderRequest;
import com.soko_backend.dto.order.OrderResponse;
import com.soko_backend.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ORDER API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Créer une commande", description = "Permet à un client de passer une commande avec une liste de produits.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur lors de la création (stock, produit, client)")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @Operation(summary = "Lister les commandes d'un client", description = "Récupère toutes les commandes passées par un client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    @Operation(summary = "Consulter une commande", description = "Retourne les détails d'une commande spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande retournée avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande introuvable")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}