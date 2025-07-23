package com.soko_backend.service;

import com.soko_backend.dto.shop.ShopRequestDto;
import com.soko_backend.dto.shop.ShopResponseDto;
import com.soko_backend.entity.shop.ShopEntity;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.exception.ResourceNotFoundException;
import com.soko_backend.repository.ShopRepository;
import com.soko_backend.security.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    private ShopRepository shopRepository;
    private CurrentUserService currentUserService;
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        shopRepository = mock(ShopRepository.class);
        currentUserService = mock(CurrentUserService.class);
        shopService = new ShopService(shopRepository, currentUserService);
    }

    @Test
    void createShop_shouldSucceed() {
        // given
        UserEntity user = UserEntity.builder().id(1L).build();
        ShopRequestDto request = new ShopRequestDto();
        request.setName("Ma Boutique");

        ShopEntity savedEntity = ShopEntity.builder()
                .id(100L)
                .name("Ma Boutique")
                .owner(user)
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(Optional.of(user));
        when(shopRepository.save(any(ShopEntity.class))).thenReturn(savedEntity);

        // when
        ShopResponseDto response = shopService.createShop(request);

        // then
        assertNotNull(response);
        assertEquals("Ma Boutique", response.getName());
        verify(shopRepository).save(any(ShopEntity.class));
    }

    @Test
    void createShop_shouldThrow_whenUserMissing() {
        // given
        when(currentUserService.getCurrentUser()).thenReturn(Optional.empty());

        // when + then
        ShopRequestDto request = new ShopRequestDto();
        request.setName("Boutique");

        assertThrows(ResourceNotFoundException.class, () -> shopService.createShop(request));
    }
}