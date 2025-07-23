package com.soko_backend.controller;


import com.soko_backend.dto.product.CreateProductRequest;
import com.soko_backend.dto.product.ProductResponse;
import com.soko_backend.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PRODUCTS API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Créer un produit",
            description = "Permet à un commerçant de créer un nouveau produit avec son identifiant (merchantId)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides ou commerçant introuvable")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestParam Long merchantId
    ) {
        return ResponseEntity.ok(productService.createProduct(request, merchantId));
    }

    @Operation(
            summary = "Lister tous les produits",
            description = "Retourne l'ensemble des produits disponibles dans la marketplace."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des produits retournée")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(
            summary = "Lister les produits d'un commerçant",
            description = "Permet de récupérer les produits publiés par un commerçant spécifique via son identifiant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des produits retournée"),
            @ApiResponse(responseCode = "404", description = "Commerçant non trouvé")
    })
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<ProductResponse>> getProductsByMerchant(@PathVariable Long merchantId) {
        return ResponseEntity.ok(productService.getProductsByMerchant(merchantId));
    }
}