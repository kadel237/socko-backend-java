// WishlistController.java
package com.soko_backend.controller;

import com.soko_backend.dto.wishlist.AddToWishlistRequest;
import com.soko_backend.dto.wishlist.WishlistResponse;
import com.soko_backend.service.wishlist.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "WISHLIST API", description = "Gestion des favoris utilisateur")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "Récupérer les favoris de l'utilisateur")
    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlistByUser());
    }

    @Operation(summary = "Ajouter un produit aux favoris")
    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(@RequestBody AddToWishlistRequest request) {
        return ResponseEntity.ok(wishlistService.addToWishlist(request));
    }

    @Operation(summary = "Supprimer un produit des favoris")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<WishlistResponse> removeFromWishlist(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(id));
    }
}
