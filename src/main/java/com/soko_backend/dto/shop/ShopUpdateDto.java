package com.soko_backend.dto.shop;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopUpdateDto {

    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String category;
    private String location;
    private Boolean active;
}