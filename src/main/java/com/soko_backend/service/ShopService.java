package com.soko_backend.service;

import com.soko_backend.dto.shop.ShopRequestDto;
import com.soko_backend.dto.shop.ShopResponseDto;
import com.soko_backend.dto.shop.ShopUpdateDto;
import com.soko_backend.entity.shop.ShopEntity;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.exception.ResourceNotFoundException;
import com.soko_backend.repository.ShopRepository;
import com.soko_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final CurrentUserService currentUserService;

    public ShopResponseDto createShop(ShopRequestDto requestDto) {
        UserEntity currentUser = currentUserService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        ShopEntity shop = ShopEntity.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .logoUrl(requestDto.getLogoUrl())
                .bannerUrl(requestDto.getBannerUrl())
                .category(requestDto.getCategory())
                .location(requestDto.getLocation())
                .owner(currentUser)
                .build();

        ShopEntity saved = shopRepository.save(shop);
        return toDto(saved);
    }

    public List<ShopResponseDto> getMyShops() {
        UserEntity currentUser = currentUserService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return shopRepository.findAllByOwner(currentUser)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ShopResponseDto updateShop(Long shopId, ShopUpdateDto updateDto) {
        UserEntity currentUser = currentUserService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        ShopEntity shop = shopRepository.findByIdAndOwner(shopId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable ou non autorisée"));


        if (updateDto.getDescription() != null) shop.setDescription(updateDto.getDescription());
        if (updateDto.getLogoUrl() != null) shop.setLogoUrl(updateDto.getLogoUrl());
        if (updateDto.getBannerUrl() != null) shop.setBannerUrl(updateDto.getBannerUrl());
        if (updateDto.getCategory() != null) shop.setCategory(updateDto.getCategory());
        if (updateDto.getLocation() != null) shop.setLocation(updateDto.getLocation());
        if (updateDto.getActive() != null) shop.setActive(updateDto.getActive());

        ShopEntity updated = shopRepository.save(shop);
        return toDto(updated);
    }

    public ShopResponseDto getShopById(Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable"));
        return toDto(shop);
    }

    private ShopResponseDto toDto(ShopEntity shop) {
        return ShopResponseDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .description(shop.getDescription())
                .logoUrl(shop.getLogoUrl())
                .bannerUrl(shop.getBannerUrl())
                .category(shop.getCategory())
                .location(shop.getLocation())
                .active(shop.isActive())
                .ownerEmail(shop.getOwner().getEmail())
                .build();
    }
}