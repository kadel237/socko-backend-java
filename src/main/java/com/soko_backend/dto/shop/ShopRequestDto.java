package com.soko_backend.dto.shop;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopRequestDto {

    @NotBlank
    private String name;

    private String description;

    private String logoUrl;

    private String bannerUrl;

    private String category;

    private String location;
}