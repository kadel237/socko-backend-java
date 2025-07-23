package com.soko_backend.controller;

import com.soko_backend.dto.shop.ShopRequestDto;
import com.soko_backend.dto.shop.ShopResponseDto;
import com.soko_backend.dto.shop.ShopUpdateDto;
import com.soko_backend.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "SHOP API ", description = "Opérations pour la gestion des boutiques")
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    @Operation(summary = "Créer une boutique", description = "Création d'une boutique pour un commerçant connecté")
    public ResponseEntity<ShopResponseDto> createShop(
            @Valid @RequestBody ShopRequestDto shopRequestDto) {
        return ResponseEntity.ok(shopService.createShop(shopRequestDto));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('MERCHANT')")
    @Operation(summary = "Lister mes boutiques", description = "Retourne la liste des boutiques appartenant au commerçant connecté")
    public ResponseEntity<List<ShopResponseDto>> getMyShops() {
        return ResponseEntity.ok(shopService.getMyShops());
    }

    @PutMapping("/{shopId}")
    @PreAuthorize("hasRole('MERCHANT')")
    @Operation(summary = "Mettre à jour une boutique", description = "Permet de modifier une boutique du commerçant")
    public ResponseEntity<ShopResponseDto> updateShop(
            @Parameter(description = "ID de la boutique") @PathVariable Long shopId,
            @Valid @RequestBody ShopUpdateDto shopUpdateDto) {
        return ResponseEntity.ok(shopService.updateShop(shopId, shopUpdateDto));
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Consulter une boutique publique", description = "Retourne les informations publiques d'une boutique")
    public ResponseEntity<ShopResponseDto> getShopById(
            @Parameter(description = "ID de la boutique") @PathVariable Long shopId) {
        return ResponseEntity.ok(shopService.getShopById(shopId));
    }
}