package com.soko_backend.dto.product;

import lombok.Data;

    @Data
    public class CreateProductRequest {
        private String name;
        private String description;
        private Double price;
        private Integer stockQuantity;
        private String imageUrl;
    }
