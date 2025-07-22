package com.soko_backend.dto.shop;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopResponseDto {

    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String category;
    private String location;
    private boolean active;
    private String ownerEmail;
}