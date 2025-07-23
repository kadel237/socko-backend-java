
package com.soko_backend.controller;

import com.soko_backend.dto.cart.*;
import com.soko_backend.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "CART API", description = "Gestion du panier utilisateur")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Récupérer le panier de l'utilisateur connecté")
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCartByUser());
    }

    @Operation(summary = "Ajouter un produit au panier")
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(request));
    }

    @Operation(summary = "Mettre à jour la quantité d'un produit dans le panier")
    @PutMapping
    public ResponseEntity<CartResponse> updateCartItem(@RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(request));
    }

    @Operation(summary = "Supprimer un produit du panier")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<CartResponse> deleteCartItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.removeCartItem(id));
    }

    @Operation(summary = "Vider complètement le panier")
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}